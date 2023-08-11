package cn.aulang.common.cache.conf;

import cn.aulang.common.cache.interceptor.CustomKeyGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
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
public class CachingAutoConfiguration implements CachingConfigurer {

    @Bean
    @Override
    @ConditionalOnMissingBean(KeyGenerator.class)
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}
