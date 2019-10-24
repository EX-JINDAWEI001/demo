package com.example.demo.component.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class GuavaDemo {

    private static final Logger logger = LoggerFactory.getLogger(GuavaDemo.class);

    private Cache<String, Object> cache;

    private static final int concurrencyLevel = 8;

    private static final long maxmumWeight = 1024;

    private static final long expireTime = 30 * 60;

    public Object fetchCache() {
        try {
            return cache.get("cacheKey", new Callable<Object>() {
                @Override
                public Object call() {
                    return fetchCacheFromOtherMethod();
                }
            });
        } catch (ExecutionException e) {
            logger.error("fetchCache error:{}", e);
            return fetchCacheFromOtherMethod();
        }
    }

    private Object fetchCacheFromOtherMethod() {
        logger.info("guava cache is timeout, get data from other way");
        return "HAHAHAHA-----GUAVACACHE";
    }

    @PostConstruct
    public void afterPropertiesSet() {
        cache = CacheBuilder.newBuilder().softValues()
                .concurrencyLevel(concurrencyLevel)
                .recordStats().maximumSize(maxmumWeight)
                .expireAfterWrite(expireTime, TimeUnit.SECONDS).build();
    }

    @PreDestroy
    public void destroy() {
        if (null != cache) {
            cache.cleanUp();
        }
    }

}
