package com.no.awards;

import io.restassured.RestAssured;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = IntegrationTestParent.Initializer.class)
public class IntegrationTestParent {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @LocalServerPort
    int localPort = 0;

    @BeforeEach
    void setup() {
        resetDatabase();
        resetRedis();
        setupRestAssured();
    }

    private void resetDatabase() {
        var tables = Stream.of(
            "employee",
            "organization",
            "activity"
        );
        tables.forEach(table -> jdbcTemplate.update("DELETE FROM " + table));
    }

    private void resetRedis() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
    }

    private void setupRestAssured() {
        RestAssured.port = localPort;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            System.setProperty("aws.accessKeyId", "id");
            System.setProperty("aws.secretAccessKey", "key");
            System.setProperty("aws.secretKey", "key");
        }
    }
}


