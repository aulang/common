package cn.aulang.common.core.tools;

import cn.aulang.common.core.utils.Systems;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Twitter Snowflake
 * <p>
 * SnowFlake的结构如下:
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 * 1bit         29bits               21bits        13bits
 */
public class Snowflake {

    private static final long MAX_MACHINE_ID = 31;

    /**
     * 开始时间截 (2021-11-01 09:00:00.000)
     */
    private final long epoch = TimeUnit.MILLISECONDS.toSeconds(1635728400000L);

    /**
     * 服务器原始ID
     */
    private final long machineId;

    /**
     * 工作机器ID(0~31)
     */
    private final long workerId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    private final BitsAllocator bitsAllocator;

    public Snowflake() {
        this(generateMachineId());
    }

    /**
     * 构造函数
     *
     * @param machineId 节点ID，注意，如果一个节点有多个实例部署，请注意区分
     */
    public Snowflake(long machineId) {
        this.machineId = machineId;
        this.workerId = getWorkerId(machineId);

        // 创建ID位数分配器
        this.bitsAllocator = new BitsAllocator(29, 10, 13);
        if (workerId > bitsAllocator.getMaxWorkerId()) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", bitsAllocator.getMaxWorkerId()));
        }
    }

    private static long generateMachineId() {
        try {
            byte[] mac = Systems.getMacAddressWithIpv4();
            long id = ((0xFF & (long) mac[mac.length - 2])
                    | (0xFF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
            id = id % (MAX_MACHINE_ID + 1);
            return id;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static long getWorkerId(long datacenterId) {
        StringBuilder sb = new StringBuilder();
        sb.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotBlank(name)) {
            sb.append(name.split("@")[0]);
        }

        return (sb.toString().hashCode() & 0xFFFF) % (MAX_MACHINE_ID + 1);
    }

    public long getMachineId() {
        return machineId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else { // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // Allocate bits for UID
        return bitsAllocator.allocate(timestamp - epoch, workerId, sequence);
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    private long timeGen() {
        long currentSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (currentSecond - epoch > bitsAllocator.getMaxDeltaSeconds()) {
            throw new RuntimeException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentSecond);
        }
        return currentSecond;
    }

    /**
     * Allocate 64 bits for the UID(long)<br>
     * sign (fixed 1bit) -> deltaSecond -> workerId -> sequence(within the same second)
     */
    private static class BitsAllocator {

        /**
         * Shift for timestamp & workerId
         */
        private final int timestampShift;
        private final int workerIdShift;

        /**
         * Max value for workId & sequence
         */
        private final long maxDeltaSeconds;
        private final long maxWorkerId;
        private final long maxSequence;

        /**
         * Constructor with timestampBits, workerIdBits, sequenceBits<br>
         * The highest bit used for sign, so <code>63</code> bits for timestampBits, workerIdBits, sequenceBits
         */
        public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {
            // initialize shift
            this.timestampShift = workerIdBits + sequenceBits;
            this.workerIdShift = sequenceBits;

            // initialize max value
            this.maxDeltaSeconds = ~(-1L << timestampBits);
            this.maxWorkerId = ~(-1L << workerIdBits);
            this.maxSequence = ~(-1L << sequenceBits);
        }

        public long getMaxDeltaSeconds() {
            return maxDeltaSeconds;
        }

        public long getMaxWorkerId() {
            return maxWorkerId;
        }

        public long getMaxSequence() {
            return maxSequence;
        }

        /**
         * Allocate bits for UID according to delta seconds & workerId & sequence<br>
         * <b>Note that: </b>The highest bit will always be 0 for sign
         *
         * @param deltaSeconds 时间戳相差毫秒数
         * @param workerId     工作机器ID
         * @param sequence     毫秒内序列
         * @return 分布式ID
         */
        public long allocate(long deltaSeconds, long workerId, long sequence) {
            return (deltaSeconds << timestampShift) | (workerId << workerIdShift) | sequence;
        }
    }
}