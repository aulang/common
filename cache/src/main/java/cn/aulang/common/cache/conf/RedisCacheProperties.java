package cn.aulang.common.cache.conf;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展Redis缓存配置
 * <p>1. 提供不同缓存失效时间配置</p>
 * <p>2. 是否开启注解里创建没有预定义缓存</p>
 *
 * @author wulang
 */
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProperties extends CacheProperties.Redis {

    /**
     * 是否开启注解里创建没有预定义缓存，默认true，不存在就创建；
     * 否则需要预先使用cacheNames和initialCaches定义好
     */
    private boolean allowInFlightCacheCreation = true;

    /**
     * 使用默认失效时间{@link CacheProperties.Redis#getTimeToLive()}的初始化缓存
     */
    private List<String> cacheNames = new ArrayList<>();

    /**
     * 自定义缓存失效时间配置，其他配置使用{@link CacheProperties.Redis}配置；
     */
    private Map<String, Duration> initialCaches = new HashMap<>();

    public boolean isAllowInFlightCacheCreation() {
        return allowInFlightCacheCreation;
    }

    public void setAllowInFlightCacheCreation(boolean allowInFlightCacheCreation) {
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    public List<String> getCacheNames() {
        return cacheNames;
    }

    public void setCacheNames(List<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    public Map<String, Duration> getInitialCaches() {
        return initialCaches;
    }

    public void setInitialCaches(Map<String, Duration> initialCaches) {
        this.initialCaches = initialCaches;
    }
}
