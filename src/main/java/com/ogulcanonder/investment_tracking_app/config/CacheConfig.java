package com.ogulcanonder.investment_tracking_app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String CACHE_NAME = "instrumentPrices";
    private static final int MAXIMUM_SIZE = 150;

    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE_NAME);

        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .expireAfter(new MarketAwareExpiry())
                .recordStats());
        return caffeineCacheManager;
    }

    static class MarketAwareExpiry implements Expiry<Object, Object> {
        private static final ZoneId ISTANBUL = ZoneId.of("Europe/Istanbul");
        private static final LocalTime MARKET_OPEN = LocalTime.of(10, 0);
        private static final LocalTime MARKET_CLOSE = LocalTime.of(18, 0);
        private static final int MARKET_OPEN_TTL_MINUTES = 15;
        private static final int MARKET_CLOSED_TTL_HOURS = 16;

        @Override
        public long expireAfterCreate(Object key, Object value, long currentTime) {
            return ttl();
        }

        @Override
        public long expireAfterUpdate(Object key, Object value, long currentTime, long currentDuration) {
            return ttl();
        }

        @Override
        public long expireAfterRead(Object key, Object value, long currentTime, long currentDuration) {
            return currentDuration;
        }

        private long ttl() {
            LocalTime now = LocalTime.now(ISTANBUL);
            if (now.isAfter(MARKET_OPEN) && now.isBefore(MARKET_CLOSE)) {
                return TimeUnit.MINUTES.toNanos(MARKET_OPEN_TTL_MINUTES);
            }
            return TimeUnit.HOURS.toNanos(MARKET_CLOSED_TTL_HOURS);
        }
    }
}
