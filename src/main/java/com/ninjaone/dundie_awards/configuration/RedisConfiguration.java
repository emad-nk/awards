package com.ninjaone.dundie_awards.configuration;

import com.ninjaone.dundie_awards.property.RedisProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching
@AllArgsConstructor
public class RedisConfiguration {

    private final RedisProperties properties;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultCacheConfiguration())
            .withInitialCacheConfigurations(buildCacheConfigurations())
            .build();
    }

    private Map<String, RedisCacheConfiguration> buildCacheConfigurations() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        for (String cacheName : CacheNames.all()) {
            cacheConfigurations.put(cacheName, defaultCacheConfiguration().entryTtl(properties.ttl().get(cacheName)));
        }
        return cacheConfigurations;
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues();
    }

    public static class CacheNames {
        public static final String DUNDIE_AWARDS = "dundie-awards";
        public static final String EMPLOYEES = "employees";

        public static List<String> all() {
            return Stream.of(
                DUNDIE_AWARDS,
                EMPLOYEES
            ).toList();
        }
    }
}
