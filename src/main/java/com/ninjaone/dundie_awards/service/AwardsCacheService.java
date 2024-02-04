package com.ninjaone.dundie_awards.service;

import static com.ninjaone.dundie_awards.configuration.RedisConfiguration.CacheNames.DUNDIE_AWARDS;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class AwardsCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmployeeRepository employeeRepository;


    // @Cacheable does not work here since increment() expecting a String value and method signature is int
    public int getTotalAwards() {
        try {
            if (cacheExists(redisTemplate.keys(DUNDIE_AWARDS))) {
                return Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(DUNDIE_AWARDS)));
            }
            var totalAwards = employeeRepository.getTotalAwards();
            redisTemplate.opsForValue().set(DUNDIE_AWARDS, String.valueOf(totalAwards));
            return totalAwards;
        } catch (Exception exception) {
            LOGGER.error("Redis failed to get total amount of awards, fallback to the DB");
            return employeeRepository.getTotalAwards();
        }
    }

    public void addAwards(Long numberOfAwards) {
        redisTemplate.opsForValue().increment(DUNDIE_AWARDS, numberOfAwards);
    }

    private boolean cacheExists(Set<String> cache){
        return cache != null && !cache.isEmpty();
    }
}
