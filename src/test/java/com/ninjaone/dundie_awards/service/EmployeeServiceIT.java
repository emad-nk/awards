package com.ninjaone.dundie_awards.service;

import static com.ninjaone.dundie_awards.Fixture.dummyEmployee;
import static com.ninjaone.dundie_awards.Fixture.dummyEmployeeRequest;
import static com.ninjaone.dundie_awards.Fixture.dummyOrganization;
import com.ninjaone.dundie_awards.IntegrationTestParent;
import static com.ninjaone.dundie_awards.configuration.RedisConfiguration.CacheNames.EMPLOYEES;
import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import com.ninjaone.dundie_awards.event.EventPublisher;
import static com.ninjaone.dundie_awards.event.Status.ADDED;
import static com.ninjaone.dundie_awards.event.Status.AWARDED;
import static com.ninjaone.dundie_awards.event.Status.REMOVED;
import static com.ninjaone.dundie_awards.event.Status.UPDATED;
import com.ninjaone.dundie_awards.exception.EntityNotFoundException;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
        var organization = dummyOrganization();
        var employee1 = dummyEmployee("Alex", "Fo", organization);
        var employee2 = dummyEmployee("Sara", "Kalin", organization);
        var employee3 = dummyEmployee("Armin", "Van Buuren", organization);

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
        var organization = dummyOrganization();
        var employee1 = dummyEmployee("Alex", "Fo", organization);
        var employee2 = dummyEmployee("Sara", "Kalin", organization);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2).toList());

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).isEmpty();

        employeeService.getAllEmployeesPaged(PageRequest.of(0, 5));

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).hasSize(1);

        employeeRepository.delete(employee1.getId());

        assertThat(redisTemplate.keys(EMPLOYEES + "*")).isEmpty();
    }
}
