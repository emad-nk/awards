package com.ninjaone.dundie_awards.service;


import static com.ninjaone.dundie_awards.Fixture.dummyEmployee;
import static com.ninjaone.dundie_awards.Fixture.dummyOrganization;
import com.ninjaone.dundie_awards.IntegrationTestParent;
import static com.ninjaone.dundie_awards.configuration.RedisConfiguration.CacheNames.DUNDIE_AWARDS;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import com.ninjaone.dundie_awards.service.AwardsCacheService;
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
        var organization = dummyOrganization();
        Integer expectedAwards = 11;
        var employee1 = dummyEmployee("Angela", "Merkel", organization, 5);
        var employee2 = dummyEmployee("Joe", "Pit", organization, 6);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        assertThat(redisTemplate.keys(DUNDIE_AWARDS)).isEmpty();

        var totalAwards = awardsCacheService.getTotalAwards();

        assertThat(totalAwards).isEqualTo(expectedAwards);

        assertThat(redisTemplate.keys(DUNDIE_AWARDS)).hasSize(1);
        assertThat(redisTemplate.opsForValue().get(DUNDIE_AWARDS)).isEqualTo(expectedAwards.toString());
    }

    @Test
    void incrementsTotalAmountOfAwardsByTwo(){
        var organization = dummyOrganization();
        var expectedAwards = 19;
        var employee1 = dummyEmployee("Angela", "Merkel", organization, 5);
        var employee2 = dummyEmployee("Joe", "Pit", organization, 14);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        var totalAwards = awardsCacheService.getTotalAwards();

        assertThat(totalAwards).isEqualTo(expectedAwards);

        awardsCacheService.addAwards(2L);

        assertThat(redisTemplate.opsForValue().get(DUNDIE_AWARDS)).isEqualTo("21");
        assertThat(awardsCacheService.getTotalAwards()).isEqualTo(21);
    }

    @Test
    void incrementsTotalAmountOfAwardsWhenCacheDoesNotExist(){
        awardsCacheService.addAwards(5L);

        assertThat(redisTemplate.opsForValue().get(DUNDIE_AWARDS)).isEqualTo("5");
        assertThat(awardsCacheService.getTotalAwards()).isEqualTo(5);
    }
}
