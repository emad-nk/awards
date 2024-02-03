package com.ninjaone.dundie_awards;

import static com.ninjaone.dundie_awards.configuration.RedisConfiguration.CacheNames.DUNDIE_AWARDS;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class AwardsCache {

    private RedisTemplate<String, String> redisTemplate;
    private EmployeeRepository employeeRepository;


    // @Cacheable does not work here since increment() expecting a String value and method signature is integer
    public Integer getTotalAwards(){
        var key = redisTemplate.keys(DUNDIE_AWARDS);
        if(key != null && !key.isEmpty()){
            return Integer.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(DUNDIE_AWARDS)));
        }
        var totalAwards =  employeeRepository.totalAwards();
        redisTemplate.opsForValue().set(DUNDIE_AWARDS, totalAwards.toString());
        return totalAwards;
    }

    public void addOneAward(){
        redisTemplate.opsForValue().increment(DUNDIE_AWARDS);
    }
}
