package com.ninjaone.dundie_awards;


import static com.ninjaone.dundie_awards.Fixture.dummyEmployee;
import static com.ninjaone.dundie_awards.Fixture.dummyOrganization;
import static com.ninjaone.dundie_awards.configuration.RedisConfiguration.CacheNames.DUNDIE_AWARDS;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AwardsCacheIT extends IntegrationTestParent{

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AwardsCache awardsCache;

    @Test
    void getsTotalAmountOfAwardsAndCachesIt(){
        var organization = dummyOrganization();
        Integer expectedAwards = 11;
        var employee1 = dummyEmployee("Angela", "Merkel", organization, 5);
        var employee2 = dummyEmployee("Joe", "Pit", organization, 6);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        assertThat(redisTemplate.keys(DUNDIE_AWARDS)).isEmpty();

        var totalAwards = awardsCache.getTotalAwards();

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

        var totalAwards = awardsCache.getTotalAwards();

        assertThat(totalAwards).isEqualTo(expectedAwards);

        awardsCache.addAwards(2L);

        assertThat(redisTemplate.opsForValue().get(DUNDIE_AWARDS)).isEqualTo("21");
        assertThat(awardsCache.getTotalAwards()).isEqualTo(21);
    }

    @Test
    void incrementsTotalAmountOfAwardsWhenCacheDoesNotExist(){
        awardsCache.addAwards(5L);

        assertThat(redisTemplate.opsForValue().get(DUNDIE_AWARDS)).isEqualTo("5");
        assertThat(awardsCache.getTotalAwards()).isEqualTo(5);
    }
}
