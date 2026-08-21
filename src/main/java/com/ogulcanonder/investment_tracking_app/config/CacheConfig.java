package com.ogulcanonder.investment_tracking_app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String CACHE_NAME = "instrumentPrices";
    private static final int MAXIMUM_SIZE = 100;
    private static final int EXPIRE_AFTER_WRITE = 60;

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE_NAME);

        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .expireAfterWrite(EXPIRE_AFTER_WRITE, TimeUnit.SECONDS)
                .recordStats());
        return caffeineCacheManager;
    }
}
