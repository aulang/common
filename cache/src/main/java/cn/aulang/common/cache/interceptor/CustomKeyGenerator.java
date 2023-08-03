package cn.aulang.common.cache.interceptor;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * 缓存key生成器
 *
 * @author wulang
 */
public class CustomKeyGenerator implements KeyGenerator {

    private static final String DELIM = "_";

    /**
     * className::methodName
     */
    private static final String PATTERN_NO_PARAM = "{0}:{1}";

    /**
     * className::methodName:param1_param2_param3
     */
    private static final String PATTERN_WITH_PARAM = "{0}:{1}:{2}";

    @Override
    public @NonNull
    Object generate(@NonNull Object target, @NonNull Method method, Object... params) {
        if (params.length == 0) {
            return MessageFormat.format(
                    PATTERN_NO_PARAM,
                    target.getClass().getSimpleName(),
                    method.getName()
            );
        } else {
            return MessageFormat.format(
                    PATTERN_WITH_PARAM,
                    target.getClass().getSimpleName(),
                    method.getName(),
                    StringUtils.arrayToDelimitedString(params, DELIM)
            );
        }
    }
}