package cn.aulang.common.cache.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 缓存异常处理器
 *
 * @author wulang
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.error("Cache get failed: {}", exception.getMessage());
        // retry ??
        throw exception;
    }

    @Override
    public void handleCachePutError(RuntimeException exception, @NonNull Cache cache,
                                    @NonNull Object key, @Nullable Object value) {
        log.error("Cache put failed: {}", exception.getMessage());
        // should we evict cache??
        throw exception;
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.error("Cache evict failed: {}", exception.getMessage());
        // retry ??
        throw exception;
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, @NonNull Cache cache) {
        log.error("Cache clear failed: {}", exception.getMessage());
        // retry ??
        throw exception;
    }
}
