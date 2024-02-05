package com.ninjaone.dundie_awards;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Entry point for starting application locally.
 * It starts the application with `local` profile so DataLoader loads some data in the DB.
 */
public class StartApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DundieAwardsApplication.class)
            .profiles("local")
            .initializers(new IntegrationTestParent.Initializer())
            .run(args);
    }
}
