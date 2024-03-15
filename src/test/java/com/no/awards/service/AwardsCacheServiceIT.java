package com.no.awards.service;


import static com.no.awards.Fixture.dummyEmployee;
import com.no.awards.IntegrationTestParent;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.repository.OrganizationRepository;
import com.no.awards.Fixture;
import com.no.awards.configuration.RedisConfiguration;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AwardsCacheServiceIT extends IntegrationTestParent {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AwardsCacheService awardsCacheService;

    @Test
    void getsTotalAmountOfAwardsAndCachesIt(){
        var organization = Fixture.dummyOrganization();
        Integer expectedAwards = 11;
        var employee1 = Fixture.dummyEmployee("Angela", "Merkel", organization, 5);
        var employee2 = Fixture.dummyEmployee("Joe", "Pit", organization, 6);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        assertThat(redisTemplate.keys(RedisConfiguration.CacheNames.AWARDS)).isEmpty();

        var totalAwards = awardsCacheService.getTotalAwards();

        assertThat(totalAwards).isEqualTo(expectedAwards);

        assertThat(redisTemplate.keys(RedisConfiguration.CacheNames.AWARDS)).hasSize(1);
        assertThat(redisTemplate.opsForValue().get(RedisConfiguration.CacheNames.AWARDS)).isEqualTo(expectedAwards.toString());
    }

    @Test
    void incrementsTotalAmountOfAwardsByTwo(){
        var organization = Fixture.dummyOrganization();
        var expectedAwards = 19;
        var employee1 = Fixture.dummyEmployee("Angela", "Merkel", organization, 5);
        var employee2 = Fixture.dummyEmployee("Joe", "Pit", organization, 14);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        var totalAwards = awardsCacheService.getTotalAwards();

        assertThat(totalAwards).isEqualTo(expectedAwards);

        awardsCacheService.addAwards(2L);

        assertThat(redisTemplate.opsForValue().get(RedisConfiguration.CacheNames.AWARDS)).isEqualTo("21");
        assertThat(awardsCacheService.getTotalAwards()).isEqualTo(21);
    }

    @Test
    void incrementsTotalAmountOfAwardsWhenCacheDoesNotExist(){
        awardsCacheService.addAwards(5L);

        assertThat(redisTemplate.opsForValue().get(RedisConfiguration.CacheNames.AWARDS)).isEqualTo("5");
        assertThat(awardsCacheService.getTotalAwards()).isEqualTo(5);
    }
}
