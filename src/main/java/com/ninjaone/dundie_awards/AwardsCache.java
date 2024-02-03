package com.ninjaone.dundie_awards;

import static com.ninjaone.dundie_awards.configuration.RedisConfiguration.CacheNames.DUNDIE_AWARDS;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class AwardsCache {

    private RedisTemplate<String, String> redisTemplate;
    private EmployeeRepository employeeRepository;


    // @Cacheable does not work here since increment() expecting a String value and method signature is int
    public int getTotalAwards() {
        try {
            if (cacheExists(redisTemplate.keys(DUNDIE_AWARDS))) {
                return Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(DUNDIE_AWARDS)));
            }
            var totalAwards = employeeRepository.totalAwards();
            redisTemplate.opsForValue().set(DUNDIE_AWARDS, totalAwards.toString());
            return totalAwards;
        } catch (Exception exception) {
            LOGGER.error("Redis failed to get total amount of awards, fallback to the DB");
            return employeeRepository.totalAwards();
        }
    }

    public void addOneAward() {
        redisTemplate.opsForValue().increment(DUNDIE_AWARDS);
    }

    private boolean cacheExists(Set<String> cache){
        return cache != null && !cache.isEmpty();
    }
}
