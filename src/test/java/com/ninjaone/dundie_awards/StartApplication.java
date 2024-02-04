package com.ninjaone.dundie_awards;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Entry point for starting application locally.
 * This is mostly helpful if Testcontainers are used instead of docker-compose-local
 */
public class StartApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DundieAwardsApplication.class)
            .profiles("local")
            .initializers(new IntegrationTestParent.Initializer())
            .run(args);
    }
}
