package com.no.awards.configuration;

import java.util.HashMap;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration implements FlywayConfigurationCustomizer {
    // Disable the transactional lock in Flyway that breaks all non-transactional migrations since v9.1.2 of the plugin
    // See https://github.com/flyway/flyway/issues/3508
    @Override
    public void customize(FluentConfiguration configuration) {
        configuration.configuration(
            new HashMap<>() {{
                put("flyway.postgresql.transactional.lock", "false");
            }}
        );
    }
}
