package cn.aulang.common.core.concurrent;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An Expired Concurrent Hash Map Solution which stores the keys and values only
 * for a specific amount of time, and then expires after that time.
 * <p>
 * The implementation is not perfect, 'remove*' methods not override, timeMap cleaned
 * by clean thread. A more powerful library Caffeine can be used in advanced scenes.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class ExpiredConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_EXPIRED_MILLIS = 1000;

    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();

    private ExpiredConcurrentHashMapListener<K, V> listener;

    private final long expiryInMillis;
    private volatile boolean alive = true;

    public ExpiredConcurrentHashMap() {
        this.expiryInMillis = DEFAULT_EXPIRED_MILLIS;
        initialize();
    }

    public ExpiredConcurrentHashMap(ExpiredConcurrentHashMapListener<K, V> listener) {
        this.listener = listener;
        this.expiryInMillis = DEFAULT_EXPIRED_MILLIS;
        initialize();
    }

    public ExpiredConcurrentHashMap(long expiryInMillis) {
        this.expiryInMillis = expiryInMillis;
        initialize();
    }

    public ExpiredConcurrentHashMap(long expiryInMillis, ExpiredConcurrentHashMapListener<K, V> listener) {
        this.expiryInMillis = expiryInMillis;
        this.listener = listener;
        initialize();
    }

    void initialize() {
        Thread thread = new CleanerThread();
        thread.setDaemon(true);
        thread.start();
    }

    public void registerListener(ExpiredConcurrentHashMapListener<K, V> listener) {
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if trying to insert values into map after quiting
     */
    @Override
    public V put(@Nonnull K key, @Nonnull V value) {
        if (!alive) {
            throw new IllegalStateException("ExpiredConcurrent Hashmap is no more alive.. Try creating a new one.");
        }
        timeMap.put(key, System.currentTimeMillis());
        V returnVal = super.put(key, value);
        Optional.ofNullable(listener).ifPresent(l -> l.onAdd(key, value));
        return returnVal;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if trying to insert values into map after quiting
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (!alive) {
            throw new IllegalStateException("ExpiredConcurrent Hashmap is no more alive.. Try creating a new one.");
        }
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if trying to insert values into map after quiting
     */
    @Override
    public V putIfAbsent(K key, V value) {
        if (!alive) {
            throw new IllegalStateException("ExpiredConcurrent Hashmap is no more alive.. Try creating a new one.");
        }
        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }
    }

    /**
     * Should call this method when it's no longer required
     */
    public void quitMap() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * This thread performs the cleaning operation on the concurrent hashmap once in a specified interval.
     * This wait interval is half of the time from the expiry time.
     */
    class CleanerThread extends Thread {

        @Override
        public void run() {
            while (alive) {
                cleanExpiredAndJunk();
                try {
                    //noinspection BusyWait
                    Thread.sleep(expiryInMillis / 2);
                } catch (InterruptedException e) {
                    quitMap();
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void cleanExpiredAndJunk() {
            long currentTime = System.currentTimeMillis();
            for (K key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key) + expiryInMillis)) {
                    V value = remove(key);
                    timeMap.remove(key);
                    //Because not override move* method, timeMap has some junk data
                    if (value != null) {
                        Optional.ofNullable(listener).ifPresent(l -> l.onRemoval(key, value));
                    }
                }
            }
        }
    }
}