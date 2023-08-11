package cn.aulang.common.cache.interceptor;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 缓存key生成器
 *
 * @author wulang
 */
public class CustomKeyGenerator implements KeyGenerator {

    private static final String DELIM = "_";
    private static final String COLON = ":";

    @Override
    public @NonNull
    Object generate(@NonNull Object target, @NonNull Method method, Object... params) {
        if (params.length == 0) {
            // className::methodName
            return target.getClass().getSimpleName() + COLON + method.getName();
        } else {
            // className::methodName:param1_param2_param3
            return target.getClass().getSimpleName() + COLON + method.getName() + COLON + StringUtils.arrayToDelimitedString(params, DELIM);
        }
    }
}