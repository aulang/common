package cn.aulang.common.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 常用日期相关方法封装，基于Apache Common Language 3
 * <p>
 * 封装的原则是让语义更符合自然语言，另外，将稍显冗杂的调用简化。
 */
public class SimpleDateUtils {

    /**
     * 返回当前日期时间值
     *
     * @return Date，当前日期时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获得当前时间的年份字段
     *
     * @return 年份
     */
    public static int year() {
        return year(calendar());
    }

    /**
     * 获得指定时间的年份字段
     *
     * @param date 指定的日期时间
     * @return 年份
     */
    public static int year(final Date date) {
        return year(calendar(date));
    }

    /**
     * 获得指定日历时间的年份字段
     *
     * @param calendar 指定的日历时间
     * @return 年份
     */
    public static int year(final Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前时间所在季度，取值从1到4
     *
     * @return 季度
     */
    public static int quarter() {
        return quarter(calendar());
    }

    /**
     * 获得指定时间所在季度，取值从1到4
     *
     * @param date 指定的日期时间
     * @return 季度
     */
    public static int quarter(final Date date) {
        return quarter(calendar(date));
    }

    /**
     * 获取指定calendar时间所在的季度，取值从1到4
     *
     * @param calendar Calendar对象
     * @return 季度
     */
    public static int quarter(final Calendar calendar) {
        return month(calendar) / 3 + 1;
    }

    /**
     * 返回当前月份序号
     * <p>
     * 注意是序号，从0开始
     *
     * @return 返回月份序号
     */
    public static int month() {
        return month(calendar());
    }

    /**
     * 返回Date对象代表时间的月份序号
     * <p>
     * 注意是序号，从0开始
     *
     * @param date 日期时间
     * @return 返回月份序号
     */
    public static int month(final Date date) {
        return month(calendar(date));
    }

    /**
     * 返回Calendar对象代表时间的月份序号
     * <p>
     * 注意是序号，从0开始
     *
     * @param calendar Calendar
     * @return 返回月份序号
     */
    public static int month(final Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获得当前时间的日期值，1到31取值范围
     *
     * @return 日期字段
     */
    public static int day() {
        return day(calendar());
    }

    /**
     * 获得指点时间的日期值，1到31取值范围
     *
     * @param date 日期时间
     * @return 日期字段
     */
    public static int day(final Date date) {
        return day(calendar(date));
    }

    /**
     * 获得当前日历时间的日期值，1到31取值范围
     *
     * @param calendar 日历时间
     * @return 日期字段
     */
    public static int day(final Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前时间的小时值
     *
     * @return 日期字段
     */
    public static int hour() {
        return hour(calendar());
    }

    /**
     * 获得指定时间的小时值
     *
     * @param date 日期时间
     * @return 小时字段
     */
    public static int hour(final Date date) {
        return hour(calendar(date));
    }

    /**
     * 获得指定日历时间的小时值
     *
     * @param calendar 日历时间
     * @return 小时字段
     */
    public static int hour(final Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得当前时间的分钟值
     *
     * @return 分钟字段
     */
    public static int minute() {
        return minute(calendar());
    }

    /**
     * 获得指定时间的分钟值
     *
     * @param date 日期时间
     * @return 分钟字段
     */
    public static int minute(final Date date) {
        return minute(calendar(date));
    }

    /**
     * 获得指定日历时间的分钟值
     *
     * @param calendar 日历时间
     * @return 分钟字段
     */
    public static int minute(final Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获得当前时间的秒值
     *
     * @return 秒字段
     */
    public static int second() {
        return second(calendar());
    }

    /**
     * 获得指定时间的秒值
     *
     * @param date 日期时间
     * @return 秒字段
     */
    public static int second(final Date date) {
        return second(calendar(date));
    }

    /**
     * 获得指定日历时间的秒值
     *
     * @param calendar 日历时间
     * @return 秒字段
     */
    public static int second(final Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取当天星期序号，遵照中国习惯，取值范围0-6, 周日=0，周六=6
     *
     * @return 星期序号
     */
    public static int dayOfWeek() {
        return dayOfWeek(calendar());
    }

    /**
     * 获取星期序号，取值范围0-6, 周日=0，周六=6
     *
     * @param date 日期时间
     * @return 星期序号
     */
    public static int dayOfWeek(final Date date) {
        return dayOfWeek(calendar(date));
    }

    /**
     * 获取星期序号，取值范围0-6, 周日=0，周六=6
     *
     * @param calendar Calendar对象
     * @return 星期序号
     */
    public static int dayOfWeek(final Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取当前周是本年度的第几周
     * <p>
     * 注意：一年最大52周，超过的则会返回1
     *
     * @return 周数
     */
    public static int weekOfYear() {
        return weekOfYear(calendar());
    }

    /**
     * 获取指定日期是年度的第几周
     * <p>
     * 注意：一年最大52周，超过的则会返回1
     *
     * @param date 指定的日期时间
     * @return 周数
     */
    public static int weekOfYear(final Date date) {
        return weekOfYear(calendar(date));
    }

    /**
     * 获取指定日历时间是年度的第几周
     * <p>
     * 注意：一年最大52周，超过的则会返回1
     *
     * @param calendar 指定当然日历时间
     * @return 周数
     */
    public static int weekOfYear(final Calendar calendar) {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取最近n个月的月份字符串数组，格式为yyyyMM
     * <p>
     * 例如[202101, 202102]
     *
     * @param count 需要的月份数量，包含当月
     * @return 月份字符串数组
     */
    public static String[] monthStrRecently(final int count) {
        Calendar calendar = Calendar.getInstance();
        String[] ret = new String[count];
        for (int i = count; i > 0; i--) {
            ret[i - 1] = DateFormatUtils.format(calendar, "yyyyMM");
            calendar.add(Calendar.MONTH, -1);
        }
        return ret;
    }

    /**
     * 获取当前月的字符串表示，格式为yyyyMM
     *
     * @return 当前月字符串表示
     */
    public static String monthStr() {
        return monthStrRecently(1)[0];
    }

    /**
     * 获取上个月的字符串表示，格式为yyyyMM
     *
     * @return 上个月的字符串表示
     */
    public static String monthStrLast() {
        return monthStrRecently(2)[0];
    }

    /**
     * 获得上上个月（Month before last)字符串表示，格式为yyyyMM
     *
     * @return 上上个月的字符串表示
     */
    public static String monthStrBeforeLast() {
        return monthStrRecently(3)[0];
    }

    /**
     * Adds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date          the date, not null
     * @param calendarField the calendar field to add to
     * @param amount        the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws NullPointerException if the date is null
     */
    public static Date offset(final Date date, final int calendarField, final int amount) {
        final Calendar c = calendar(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 按年调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的年数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetYear(final Date date, final int amount) {
        return DateUtils.addYears(Objects.requireNonNull(date), amount);
    }

    /**
     * 按月调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的月份数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetMonth(final Date date, final int amount) {
        return DateUtils.addMonths(date, amount);
    }

    /**
     * 按星期调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的星期数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetWeek(final Date date, final int amount) {
        return DateUtils.addWeeks(date, amount);
    }

    /**
     * 按天调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的天数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetDay(final Date date, final int amount) {
        return DateUtils.addDays(date, amount);
    }

    /**
     * 按小时调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的小时数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetHour(final Date date, final int amount) {
        return DateUtils.addHours(date, amount);
    }

    /**
     * 按分钟调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的分钟数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetMinute(final Date date, final int amount) {
        return DateUtils.addMinutes(date, amount);
    }

    /**
     * 按秒调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的秒数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetSecond(final Date date, final int amount) {
        return DateUtils.addSeconds(date, amount);
    }

    /**
     * 按毫秒调整指定时间
     *
     * @param date   待调整时间
     * @param amount 调整的毫秒数，整数和负数均可，正数向未来方向调整，负数反之
     * @return 调整好的时间
     */
    public static Date offsetMillisecond(final Date date, final int amount) {
        return DateUtils.addMilliseconds(date, amount);
    }

    /**
     * 精度调整，将field指定之后的字段全部设置为0
     *
     * @param date  日期时间
     * @param field 精度对应的字段
     * @return 重置之后的时间
     * @see DateUtils#truncate(Date, int)
     */
    public static Date truncate(final Date date, final int field) {
        return DateUtils.truncate(date, field);
    }

    /**
     * <p>Gets a date ceiling, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the date-time of 28 Mar 2002
     * 13:45:01.231, if you passed with HOUR, it would return 28 Mar
     * 2002 14:00:00.000.  If this was passed with MONTH, it would
     * return 1 Apr 2002 0:00:00.000.</p>
     *
     * @param date  the date to work with, not null
     * @param field the field from {@code Calendar} or {@code SEMI_MONTH}
     * @return the different ceil date, not null
     * @throws NullPointerException if the date is {@code null}
     * @throws ArithmeticException  if the year is over 280 million
     */
    public static Date ceiling(final Date date, final int field) {
        return DateUtils.ceiling(date, field);
    }

    /**
     * 获取秒级别的开始时间，即忽略毫秒部分
     *
     * @param date 日期
     * @return 忽略毫秒之后的时间
     */
    public static Date beginOfSecond(final Date date) {
        return truncate(date, Calendar.SECOND);
    }

    /**
     * 获取秒级别的结束时间，即毫秒设置为999
     *
     * @param date 日期
     * @return 秒级的结束时间
     */
    public static Date endOfSecond(final Date date) {
        return offsetMillisecond(DateUtils.ceiling(date, Calendar.SECOND), -1);
    }

    /**
     * 获取分钟级别的开始时间
     *
     * @param date 日期
     * @return 分钟级的开始时间
     */
    public static Date beginOfMinute(final Date date) {
        return truncate(date, Calendar.MINUTE);
    }

    /**
     * 获取分钟级别的结束时间
     *
     * @param date 日期
     * @return 分钟级的结束时间
     */
    public static Date endOfMinute(final Date date) {
        return offsetMillisecond(DateUtils.ceiling(date, Calendar.MINUTE), -1);
    }

    /**
     * 获取当前时间小时级别的开始时间
     *
     * @return 小时级的开始时间
     */
    public static Date beginOfHour() {
        return beginOfHour(new Date());
    }

    /**
     * 获取小时级别的开始时间，即忽略毫秒部分
     *
     * @param date 日期
     * @return 小时级的开始时间
     */
    public static Date beginOfHour(final Date date) {
        return truncate(date, Calendar.HOUR);
    }

    /**
     * 获取当前时间小时级别的结束时间
     *
     * @return 小时级别的结束时间
     */
    public static Date endOfHour() {
        return endOfHour(new Date());
    }

    /**
     * 获取小时级别的结束时间
     *
     * @param date 日期
     * @return 小时精度级别的结束时间
     */
    public static Date endOfHour(final Date date) {
        return offsetMillisecond(DateUtils.ceiling(date, Calendar.HOUR), -1);
    }

    /**
     * 获取当天的开始时间
     *
     * @return 一天的开始时间
     */
    public static Date beginOfDay() {
        return beginOfDay(new Date());
    }

    /**
     * 获取某天的开始时间
     *
     * @param date 日期
     * @return 一天的开始时间
     */
    public static Date beginOfDay(final Date date) {
        return truncate(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当天的结束时间
     *
     * @return 一天的结束时间
     */
    public static Date endOfDay() {
        return endOfDay(new Date());
    }

    /**
     * 获取某天的结束时间
     *
     * @param date 日期
     * @return 一天的结束时间
     */
    public static Date endOfDay(final Date date) {
        return offsetMillisecond(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    /**
     * 获取当前周的开始时间，周一定为一周的开始时间
     *
     * @return 一周的开始时间
     */
    public static Date beginOfWeek() {
        return beginOfWeek(new Date());
    }

    /**
     * 获取给定日期所在周的开始时间，周一定为一周的开始时间
     *
     * @param date 日期
     * @return 一周的开始时间
     */
    public static Date beginOfWeek(final Date date) {
        return beginOfWeek(date, true);
    }

    /**
     * 获取给定日期所在周的开始时间
     *
     * @param date               日期
     * @param isMondayAsFirstDay 是否周一做为一周的第一天（false表示周日做为第一天）
     * @return 一周的开始时间，根据isMondayAsFirstDay得到不同的时间
     */
    public static Date beginOfWeek(final Date date, final boolean isMondayAsFirstDay) {
        Calendar calendar = calendar(date);
        calendar.setFirstDayOfWeek(isMondayAsFirstDay ? Calendar.MONDAY : Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return truncate(calendar.getTime(), Calendar.DATE);
    }

    /**
     * 获取当前的结束时间，周日定为一周的结束
     *
     * @return 一周的结束时间
     */
    public static Date endOfWeek() {
        return endOfWeek(new Date(), true);
    }

    /**
     * 获取某周的结束时间，周日定为一周的结束
     *
     * @param date 日期
     * @return 一周的结束时间
     */
    public static Date endOfWeek(final Date date) {
        return endOfWeek(date, true);
    }

    /**
     * 获取某周的结束时间
     *
     * @param date              日期
     * @param isSundayAsLastDay 是否周日做为一周的最后一天（false表示周六做为最后一天）
     * @return 一周的结束时间，根据isSundayAsLastDay获得不同的时间
     */
    public static Date endOfWeek(final Date date, final boolean isSundayAsLastDay) {
        Calendar calendar = calendar(date);
        calendar.setFirstDayOfWeek(isSundayAsLastDay ? Calendar.MONDAY : Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, isSundayAsLastDay ? Calendar.SUNDAY : Calendar.SATURDAY);
        return endOfDay(calendar.getTime());
    }

    /**
     * 获取某月的开始时间
     *
     * @return 月份的开始时间
     */
    public static Date beginOfMonth() {
        return truncate(new Date(), Calendar.MONTH);
    }

    /**
     * 获取某月的开始时间
     *
     * @param date 日期
     * @return 月份的开始时间
     */
    public static Date beginOfMonth(final Date date) {
        return truncate(date, Calendar.MONTH);
    }

    /**
     * 获取当月的结束时间
     *
     * @return 月份的结束时间
     */
    public static Date endOfMonth() {
        return endOfMonth(new Date());
    }

    /**
     * 获取某月的结束时间
     *
     * @param date 日期
     * @return 月份的结束时间
     */
    public static Date endOfMonth(final Date date) {
        return offsetMillisecond(DateUtils.ceiling(date, Calendar.MONTH), -1);
    }

    /**
     * 获取当前季度的开始时间
     *
     * @return 季度的开始时间
     */
    public static Date beginOfQuarter() {
        return beginOfQuarter(new Date());
    }

    /**
     * 获取某季度的开始时间
     *
     * @param date 日期
     * @return 季度的开始时间
     */
    public static Date beginOfQuarter(final Date date) {
        Calendar calendar = calendar(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) / 3 * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return beginOfDay(calendar.getTime());
    }

    /**
     * 获取当前季度的结束时间
     *
     * @return 季度的结束时间
     */
    public static Date endOfQuarter() {
        return endOfQuarter(new Date());
    }

    /**
     * 获取某季度的结束时间
     *
     * @param date 日期
     * @return 季度的结束时间
     */
    public static Date endOfQuarter(final Date date) {
        Calendar calendar = calendar(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) / 3 * 3 + 2);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return endOfDay(calendar.getTime());
    }

    /**
     * 获取当前时间所在半年的开始
     *
     * @return 半年的开始时间
     */
    public static Date beginOfHalfYear() {
        return beginOfHalfYear(new Date());
    }

    /**
     * 获取某半年的开始时间
     *
     * @param date 日期
     * @return 半年的开始时间
     */
    public static Date beginOfHalfYear(final Date date) {
        if (isFirstHalfOfYear(date)) {
            return beginOfYear(date);
        }
        Calendar calendar = calendar(date);
        calendar.set(Calendar.MONTH, 6);
        return DateUtils.truncate(calendar.getTime(), Calendar.MONTH);
    }

    /**
     * 获得当前所在半年的结束时间
     *
     * @return 半年的结束时间
     */
    public static Date endOfHalfYear() {
        return endOfHalfYear(new Date());
    }

    /**
     * 获得某半年的结束时间
     *
     * @param date 日期
     * @return 半年的结束时间
     */
    public static Date endOfHalfYear(final Date date) {
        Calendar calendar = calendar(date);
        calendar.set(Calendar.MONTH, isFirstHalfOfYear(date) ? 5 : 11);
        return offsetMillisecond(DateUtils.ceiling(calendar.getTime(), Calendar.MONTH), -1);
    }

    /**
     * 获取当年的开始时间
     *
     * @return 年的开始时间
     * @see DateUtils#truncate(Date, int)
     */
    public static Date beginOfYear() {
        return DateUtils.truncate(new Date(), Calendar.YEAR);
    }

    /**
     * 获取某年的开始时间
     *
     * @param date 日期
     * @return 年的开始时间
     * @see DateUtils#truncate(Date, int)
     */
    public static Date beginOfYear(final Date date) {
        return DateUtils.truncate(date, Calendar.YEAR);
    }

    /**
     * 获取当年的结束时间，精确到毫秒
     *
     * @return 年的结束时间
     * @see DateUtils#ceiling(Date, int)
     */
    public static Date endOfYear() {
        return endOfYear(new Date());
    }

    /**
     * 获取某年的结束时间
     *
     * @param date 日期
     * @return 年的结束时间
     */
    public static Date endOfYear(final Date date) {
        return offsetMillisecond(DateUtils.ceiling(date, Calendar.YEAR), -1);
    }

    /**
     * 判断指定时间是否为上半年
     *
     * @param date 时间
     * @return 是否为上半年 true 是 false 不是
     */
    public static boolean isFirstHalfOfYear(final Date date) {
        //月份从0开始索引，6是七月
        return month() < 6;
    }

    /**
     * 昨天
     *
     * @return 昨天
     */
    public static Date yesterday() {
        return offsetDay(new Date(), -1);
    }

    /**
     * 明天
     *
     * @return 明天
     */
    public static Date tomorrow() {
        return offsetDay(new Date(), 1);
    }

    /**
     * 上周
     *
     * @return 上周
     */
    public static Date lastWeek() {
        return offsetWeek(new Date(), -1);
    }

    /**
     * 下周
     *
     * @return 下周
     */
    public static Date nextWeek() {
        return offsetWeek(new Date(), 1);
    }

    /**
     * 上个月
     *
     * @return 上个月
     */
    public static Date lastMonth() {
        return offsetMonth(new Date(), -1);
    }

    /**
     * 下个月
     *
     * @return 下个月
     */
    public static Date nextMonth() {
        return offsetMonth(new Date(), 1);
    }

    /**
     * 本年是否为闰年
     *
     * @return 是否闰年
     */
    public static boolean isLeapYear() {
        return isLeapYear(year());
    }

    /**
     * 指定日期是否闰年
     *
     * @param date 日期
     * @return 是否闰年
     */
    public static boolean isLeapYear(final Date date) {
        return isLeapYear(year(date));
    }

    /**
     * 指定年份是否闰年
     *
     * @param year 年
     * @return 是否闰年
     */
    public static boolean isLeapYear(final int year) {
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * 计算两个时间之间的间隔, 最大精度毫秒
     * <p>
     * 注意：如果TimeUnit设置粒度较大，比如天，则有余数的会被忽略
     *
     * @param start    起始时间
     * @param end      结束时间
     * @param timeUnit 时间单位
     * @return 时间间隔n，单位是dateUnit指定的，可以是月、天、小时、分、秒、毫秒等
     * 超出毫秒的精度，例如微秒，则抛出{@code IllegalArgumentException}
     */
    public static long between(final Date start, final Date end, final TimeUnit timeUnit) {
        long diff = end.getTime() - start.getTime();
        long millisWeight = timeUnit.toMillis(1);
        if (millisWeight == 0) {
            throw new IllegalArgumentException("between method Not support " + timeUnit);
        }
        return diff / millisWeight;
    }

    /**
     * 判断两个时间是否是同一天
     *
     * @param date1 待判断的时间一
     * @param date2 待判断的时间二
     * @return true 是一天，false 不是同一天
     */
    public static boolean isSameDay(final Date date1, final Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * 判断两个时间是否是同一天
     *
     * @param cal1 待判断的日历时间一
     * @param cal2 待判断的日历时间二
     * @return true 是一天，false 不是同一天
     */
    public static boolean isSameDay(final Calendar cal1, final Calendar cal2) {
        return DateUtils.isSameDay(cal1, cal2);
    }

    /**
     * 判断两个时间之间相隔的天数，只关注日期，而不是实际时间间隔
     * <p>
     * 比如2001-12-01 23:59:59 和 2001-12-02 00:00:00相隔1天
     *
     * @param date1 待判断的日期时间一
     * @param date2 待判断的日期时间二
     * @return 日期相隔的天数
     */
    public static long elapseDays(final Date date1, final Date date2) {
        return between(beginOfDay(date1), beginOfDay(date2), TimeUnit.DAYS);
    }

    /**
     * 判断当前时间是否为上午
     *
     * @return true 上午 false 下午
     */
    public static boolean isAM() {
        return isAM(Calendar.getInstance());
    }

    /**
     * 判断指定时间是否为上午
     *
     * @param date 日期时间
     * @return true 上午 false 下午
     */
    public static boolean isAM(final Date date) {
        return isAM(calendar(date));
    }

    /**
     * 判断当前时间是否为上午
     *
     * @param calendar 日历时间
     * @return true 上午 false 下午
     */
    public static boolean isAM(final Calendar calendar) {
        return calendar.get(Calendar.AM_PM) == Calendar.AM;
    }

    /**
     * 判断当前时间是否为下午
     *
     * @return true 下午 false 上午
     */
    public static boolean isPM() {
        return isPM(Calendar.getInstance());
    }

    /**
     * 判断指定时间是否为下午
     *
     * @param date 日期时间
     * @return true 下午 false 上午
     */
    public static boolean isPM(final Date date) {
        return isPM(calendar(date));
    }

    /**
     * 判断当前时间是否为下午
     *
     * @param calendar 日历时间
     * @return true 下午 false 上午
     */
    public static boolean isPM(final Calendar calendar) {
        return calendar.get(Calendar.AM_PM) == Calendar.PM;
    }

    /**
     * 获取当前日历时间
     *
     * @return 日历时间
     */
    public static Calendar calendar() {
        return Calendar.getInstance();
    }

    /**
     * 将日期时间转为日历时间
     *
     * @param date 日期时间
     * @return 日历时间
     */
    public static Calendar calendar(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 根据年份数字获得日期对象
     *
     * @param year 年份
     * @return 返回一年的开始时间
     */
    public static Date of(final int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return beginOfYear(calendar.getTime());
    }

    /**
     * 根据年份和月份数字获得日期对象
     *
     * @param year  年份
     * @param month 月份序号，注意是从0开始
     * @return 返回月份的开始时间
     */
    public static Date of(final int year, final int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return beginOfMonth(calendar.getTime());
    }

    /**
     * 根据年份和月份、日期数字获得日期对象
     *
     * @param year  年份
     * @param month 月份序号，注意是从0开始
     * @param date  日期
     * @return 返回对应日期的开始时间
     */
    public static Date of(final int year, final int month, final int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return beginOfDay(calendar.getTime());
    }

    /**
     * 根据年份和月份、日期以及小时和分钟数字获得日期对象
     *
     * @param year      年份
     * @param month     月份序号，注意是从0开始
     * @param date      日期
     * @param hourOfDay 小时时刻
     * @param minute    分钟数
     * @return 返回对应日期的开始时间
     */
    public static Date of(int year, int month, int date, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute);
        return beginOfMinute(calendar.getTime());
    }

    /**
     * 根据年份和月份、日期以及小时和分钟、秒数字获得日期对象
     *
     * @param year      年份
     * @param month     月份序号，注意是从0开始
     * @param date      日期
     * @param hourOfDay 小时时刻
     * @param minute    分钟数
     * @param second    秒数
     * @return 返回对应日期的开始时间
     */
    public static Date of(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hourOfDay, minute, second);
        return beginOfSecond(calendar.getTime());
    }

    /**
     * 解析时间字符串为时间, 默认支持如下格式
     * <p>
     * yyyy-MM-dd'T'HH:mm:ssXXX, yyyy-MM-dd HH:mm:ss.SSS,
     * yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, yyyy-MM
     *
     * @param str 时间字符串
     * @return 日期时间
     * @throws ParseException 当无法解释时间字符串的时候，抛出
     * @see DatePattern
     */
    public static Date parse(final String str) throws ParseException {
        return DateUtils.parseDate(str, DatePattern.GENERAL_DATETIME_PATTERNS);
    }

    /**
     * 解析时间字符串为时间
     *
     * @param str     时间字符串
     * @param pattern 时间格式，可以将常用的格式以数组或可变参数传递进来，自动尝试所有格式
     * @return 日期时间
     * @throws ParseException 当无法解释时间字符串的时候，抛出
     */
    public static Date parse(final String str, final String... pattern) throws ParseException {
        return DateUtils.parseDate(str, pattern);
    }

    /**
     * 格式化时间格式为字符串，格式为DATETIME_PATTERN(yyyy-MM-dd HH:mm:ss)
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String format(final Date date) {
        return DateFormatUtils.format(date, DatePattern.DATETIME_PATTERN);
    }

    /**
     * 格式化时间格式为字符串
     *
     * @param date    时间
     * @param pattern 格式字符串
     * @return 时间字符串
     */
    public static String format(final Date date, final String pattern) {
        return date == null ? "" : DateFormatUtils.format(date, pattern);
    }

    /**
     * 格式化日历时间格式为字符串，格式为DATETIME_PATTERN(yyyy-MM-dd HH:mm:ss)
     *
     * @param calendar 日历时间
     * @return 时间字符串
     */
    public static String format(final Calendar calendar) {
        return format(calendar.getTime());
    }

    /**
     * 格式化日历时间格式为字符串
     *
     * @param calendar 日历时间
     * @param pattern  格式字符串
     * @return 时间字符串
     */
    public static String format(final Calendar calendar, final String pattern) {
        return calendar == null ? "" : DateFormatUtils.format(calendar, pattern);
    }

    /**
     * 将Date对象进行包装，增强其时间先后的判断能力
     *
     * @param date 待包装的时间
     * @return 新的增强对象
     */
    public static DateWrapper wrapper(final Date date) {
        return new DateWrapper(date);
    }

    /**
     * 增强java.util.Date功能
     */
    public static class DateWrapper {

        private final Date date;

        DateWrapper(Date date) {
            this.date = date;
        }

        public Date date() {
            return date;
        }

        /**
         * 当前日期是否在日期指定范围内
         *
         * @param beginDate 起始日期（包含）
         * @param endDate   结束日期（包含）
         * @return 当前时间是否在范围内
         */
        public boolean isIn(final Date beginDate, final Date endDate) {
            long beginMills = beginDate.getTime();
            long endMills = endDate.getTime();
            long currentMillis = date.getTime();

            return currentMillis >= Math.min(beginMills, endMills)
                    && currentMillis <= Math.max(beginMills, endMills);
        }

        /**
         * 当前是否在给定日期之前
         *
         * @param date 日期
         * @return 是否在给定日期之前
         */
        public boolean isBefore(final Date date) {
            return this.date.compareTo(date) < 0;
        }

        /**
         * 是否在给定日期之前或与给定日期相等
         *
         * @param date 日期
         * @return 是否在给定日期之前或与给定日期相等
         */
        public boolean isBeforeOrEquals(final Date date) {
            return this.date.compareTo(date) <= 0;
        }

        /**
         * 是否在给定日期之后
         *
         * @param date 日期
         * @return 是否在给定日期之后
         */
        public boolean isAfter(final Date date) {
            return this.date.compareTo(date) > 0;
        }

        /**
         * 是否在给定日期之后或与给定日期相等
         *
         * @param date 日期
         * @return 是否在给定日期之后或与给定日期相等
         */
        public boolean isAfterOrEquals(final Date date) {
            return this.date.compareTo(date) >= 0;
        }
    }

}