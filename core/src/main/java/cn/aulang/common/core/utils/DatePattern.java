package cn.aulang.common.core.utils;

public interface DatePattern {

    String DATE_PATTERN_MONTH = "yyyy-MM";

    String DATE_PATTERN = "yyyy-MM-dd";

    String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    String DATETIME_PATTERN_N = "yyyyMMddHHmmss";

    String FULL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";

    String TIME_PATTERN_MINUTE = "HH:mm";

    String TIME_PATTERN = "HH:mm:ss";


    /**
     * 常用的日期时间格式.
     */
    String[] GENERAL_DATETIME_PATTERNS = {
            UTC_PATTERN,
            FULL_DATETIME_PATTERN,
            DATETIME_PATTERN,
            DATETIME_PATTERN_N,
            DATE_PATTERN,
            DATE_PATTERN_MONTH
    };
}
