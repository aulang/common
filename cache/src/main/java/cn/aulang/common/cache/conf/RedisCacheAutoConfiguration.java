package cn.aulang.common.cache.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Redis 缓存自动装配
 *
 * @author wulang
 */
@Configuration
@EnableConfigurationProperties(RedisCacheProperties.class)
@ConditionalOnClass({Redisson.class, RedisOperations.class})
public class RedisCacheAutoConfiguration {

    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisCacheProperties redisCacheProperties,
                                          RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = createConfiguration(redisCacheProperties);

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration);

        // 没有配置失效时间的配置的初始化缓存，使用同一个默认配置configuration
        List<String> cacheNames = redisCacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        }

        // 配置了失效时间的初始化缓存，不能使用同一个配置configuration，否则对象引用会修改干扰
        Map<String, Duration> initialCaches = redisCacheProperties.getInitialCaches();
        if (!initialCaches.isEmpty()) {
            initialCaches.forEach((k, v) ->
                    builder.withCacheConfiguration(k, createConfiguration(redisCacheProperties).entryTtl(v)));
        }

        // 是否开启注解里创建没有预定义缓存
        if (!redisCacheProperties.isAllowInFlightCacheCreation()) {
            builder.disableCreateOnMissingCache();
        }

        if (redisCacheProperties.isEnableStatistics()) {
            builder.enableStatistics();
        }

        return builder.build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueSerializer = createValueSerializer();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private RedisCacheConfiguration createConfiguration(RedisCacheProperties redisCacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueSerializer = createValueSerializer();

        config = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));

        if (redisCacheProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisCacheProperties.getTimeToLive());
        }
        if (redisCacheProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisCacheProperties.getKeyPrefix());
        }
        if (!redisCacheProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisCacheProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }

    private RedisSerializer<Object> createValueSerializer() {
        JsonMapper jsonMapper = JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                        ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .build();

        return new Jackson2JsonRedisSerializer<>(jsonMapper, Object.class);
    }
}
