package com.ninjaone.dundie_awards;

import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
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

    private void resetRedis(){
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
    }



    private void setupRestAssured() {
        RestAssured.port = localPort;
    }
}


