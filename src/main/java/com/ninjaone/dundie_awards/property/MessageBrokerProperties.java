package com.ninjaone.dundie_awards.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("message-broker")
public record MessageBrokerProperties(
        Long delayMilliseconds
) {
}
