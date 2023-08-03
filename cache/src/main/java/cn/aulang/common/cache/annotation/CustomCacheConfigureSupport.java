package cn.aulang.common.cache.annotation;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * 默认CachingConfigurer配置
 *
 * @author wulang
 */
public record CustomCacheConfigureSupport(KeyGenerator keyGenerator, CacheErrorHandler errorHandler) implements CachingConfigurer {

}
