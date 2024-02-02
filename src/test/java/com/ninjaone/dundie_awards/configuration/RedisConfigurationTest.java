package com.ninjaone.dundie_awards.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class RedisConfigurationTest {

    @Test
    void hasTheRightNumberOfCaches(){
        assertThat(RedisConfiguration.CacheNames.all())
                .hasSize(1)
                .contains("dundie-awards");
    }

}