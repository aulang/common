package cn.aulang.common.cache.conf;

import cn.aulang.common.cache.annotation.CustomCacheConfigureSupport;
import cn.aulang.common.cache.interceptor.CustomCacheErrorHandler;
import cn.aulang.common.cache.interceptor.CustomKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存基础（key生成器和异常处理器）自动配置
 *
 * @author wulang
 */
@EnableCaching
@Configuration
public class CachingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(KeyGenerator.class)
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(CacheErrorHandler.class)
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean(CachingConfigurer.class)
    public CachingConfigurer cachingConfigurer(
            @Autowired(required = false) KeyGenerator keyGenerator,
            @Autowired(required = false) CacheErrorHandler errorHandler) {
        return new CustomCacheConfigureSupport(keyGenerator, errorHandler);
    }
}
