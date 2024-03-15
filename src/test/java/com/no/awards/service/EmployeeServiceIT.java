package com.no.awards.service;

import static com.no.awards.Fixture.dummyEmployee;
import com.no.awards.IntegrationTestParent;
import static com.no.awards.configuration.RedisConfiguration.CacheNames.EMPLOYEES;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.repository.OrganizationRepository;
import com.no.awards.Fixture;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;

class EmployeeServiceIT extends IntegrationTestParent {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void evictsEmployeesCacheWhenEmployeesTableGetsANewUpdate() {
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("Alex", "Fo", organization);
        var employee2 = Fixture.dummyEmployee("Sara", "Kalin", organization);
        var employee3 = Fixture.dummyEmployee("Armin", "Van Buuren", organization);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).isEmpty();

        employeeService.getAllEmployeesPaged(PageRequest.of(0, 5));

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).hasSize(1);

        employeeRepository.save(employee3);

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).isEmpty();
    }

    @Test
    void evictsEmployeesCacheWhenAnEmployeeGetsDeleted() {
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("Alex", "Fo", organization);
        var employee2 = Fixture.dummyEmployee("Sara", "Kalin", organization);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).isEmpty();

        employeeService.getAllEmployeesPaged(PageRequest.of(0, 5));

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).hasSize(1);

        employeeRepository.delete(employee1.getId());

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).isEmpty();
    }
}
