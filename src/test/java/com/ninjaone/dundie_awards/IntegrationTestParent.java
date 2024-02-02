package com.ninjaone.dundie_awards;

import io.restassured.RestAssured;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTestParent {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int localPort = 0;

    @BeforeEach
    void setup() {
        resetDatabase();
        setupRestAssured();
    }

    void resetDatabase() {
        var tables = new ArrayList<String>(){
            {
                add("employee");
                add("organization");
                add("activity");
            }
        };
        tables.forEach(table -> jdbcTemplate.update("DELETE FROM " + table));
    }

    void setupRestAssured() {
        RestAssured.port = localPort;
    }
}


