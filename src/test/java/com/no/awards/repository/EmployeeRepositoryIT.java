package com.no.awards.repository;

import static com.no.awards.Fixture.dummyEmployee;
import com.no.awards.IntegrationTestParent;
import com.no.awards.Fixture;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
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
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("John", "Paul", organization);
        var employee2 = Fixture.dummyEmployee("Mark", "Haul", organization);
        var employee3 = Fixture.dummyEmployee("Angelina", "Pitt", organization);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2, employee3).toList());

        var page1 = employeeRepository.getEmployees(PageRequest.of(0, 2, Sort.by("last_name")));
        var page2 = employeeRepository.getEmployees(PageRequest.of(1, 2, Sort.by("last_name")));

        assertThat(page1.getTotalElements()).isEqualTo(3);
        assertThat(page1.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page1.get()).hasSize(2);
        Assertions.assertThat(page1.get()).containsExactly(employee2, employee1);

        Assertions.assertThat(page2.get()).hasSize(1);
        Assertions.assertThat(page2.get()).containsExactly(employee3);
    }

    @Test
    void getsSumOfAllTheAwards() {
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("John", "Paul", organization, 1);
        var employee2 = Fixture.dummyEmployee("Mark", "Haul", organization, 10);
        var employee3 = Fixture.dummyEmployee("Angelina", "Pitt", organization, 5);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2, employee3).toList());

        assertThat(employeeRepository.getTotalAwards()).isEqualTo(16);
    }

    @Test
    void deletesAnEmployeeById() {
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("John", "Paul", organization, 1);
        var employee2 = Fixture.dummyEmployee("Mark", "Haul", organization, 10);
        var employee3 = Fixture.dummyEmployee("Angelina", "Pitt", organization, 5);

        organizationRepository.save(organization);
        employeeRepository.saveAll(Stream.of(employee1, employee2, employee3).toList());

        Assertions.assertThat(employeeRepository.findAll()).hasSize(3);

        employeeRepository.delete(employee1.getId());

        var remainingEmployees = employeeRepository.findAll();

        Assertions.assertThat(remainingEmployees).hasSize(2);
        Assertions.assertThat(remainingEmployees).doesNotContain(employee1);
    }

    @Test
    void getsZeroForSumOfAllTheAwardsWhenThereAreNoEntriesInTheTable() {
        assertThat(employeeRepository.getTotalAwards()).isEqualTo(0);
    }
}
