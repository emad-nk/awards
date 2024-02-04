package com.ninjaone.dundie_awards.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    private static final String API_VERSION = "0.1";
    private static final String SERVICE_DESCRIPTION = "Serves employees with their dundie awards";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("dudnie-awards")
                .description(SERVICE_DESCRIPTION)
                .version(API_VERSION));
    }
}
