package com.ninjaone.dundie_awards.repository;

import static com.ninjaone.dundie_awards.Fixture.dummyEmployee;
import static com.ninjaone.dundie_awards.Fixture.dummyOrganization;
import com.ninjaone.dundie_awards.IntegrationTestParent;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class EmployeeRepositoryIT extends IntegrationTestParent {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void getsEmployeesPaginated() {
        var organization = dummyOrganization();
        var employee1 = dummyEmployee("John", "Paul", organization);
        var employee2 = dummyEmployee("Mark", "Haul", organization);
        var employee3 = dummyEmployee("Angelina", "Pitt", organization);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2, employee3).toList());

        var page1 = employeeRepository.getEmployees(PageRequest.of(0, 2, Sort.by("last_name")));
        var page2 = employeeRepository.getEmployees(PageRequest.of(1, 2, Sort.by("last_name")));

        assertThat(page1.getTotalElements()).isEqualTo(3);
        assertThat(page1.getTotalPages()).isEqualTo(2);
        assertThat(page1.get()).hasSize(2);
        assertThat(page1.get()).containsExactly(employee2, employee1);

        assertThat(page2.get()).hasSize(1);
        assertThat(page2.get()).containsExactly(employee3);
    }

    @Test
    void getsSumOfAllTheAwards() {
        var organization = dummyOrganization();
        var employee1 = dummyEmployee("John", "Paul", organization, 1);
        var employee2 = dummyEmployee("Mark", "Haul", organization, 10);
        var employee3 = dummyEmployee("Angelina", "Pitt", organization, 5);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2, employee3).toList());

        assertThat(employeeRepository.getTotalAwards()).isEqualTo(16);
    }

    @Test
    void getsZeroForSumOfAllTheAwardsWhenThereAreNoEntriesInTheTable() {
        assertThat(employeeRepository.getTotalAwards()).isEqualTo(0);
    }
}
