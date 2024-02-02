package com.ninjaone.dundie_awards.property;

import java.time.Duration;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis")
public record RedisProperties(
        Map<String, Duration> ttl
) {
}