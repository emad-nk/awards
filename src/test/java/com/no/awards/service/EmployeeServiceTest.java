package com.no.awards.service;

import static com.no.awards.Fixture.dummyEmployee;
import com.no.awards.controller.dto.response.EmployeeDTO;
import com.no.awards.event.EventPublisher;
import static com.no.awards.event.Status.ADDED;
import static com.no.awards.event.Status.AWARDED;
import static com.no.awards.event.Status.REMOVED;
import static com.no.awards.event.Status.UPDATED;
import com.no.awards.exception.EntityNotFoundException;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.Fixture;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getsListOfEmployeesPaginated() {
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("Alex", "Fo", organization);
        var employee2 = Fixture.dummyEmployee("Sara", "Kalin", organization);
        var expectedResult = new PageImpl<>(
            Stream.of(employee1, employee2).toList(),
            PageRequest.of(0, 5),
            2
        );

        when(employeeRepository.getEmployees(PageRequest.of(0, 5))).thenReturn(expectedResult);

        var result = employeeService.getAllEmployeesPaged(PageRequest.of(0, 5));

        assertThat(result.stream().toList()).hasSize(2);
        assertThat(result.stream().map(EmployeeDTO::lastName).toList()).containsExactly("Fo", "Kalin");
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void createsAnEmployeeAndPublishesItToEventPublisher() {
        var organization = Fixture.dummyOrganization();
        var employeeRequest = Fixture.dummyEmployeeRequest("Alex", "Fo", organization);
        var employee = Fixture.dummyEmployee("Alex", "Fo", organization);
        var employeeDTO = employee.toEmployeeDTO();

        when(employeeRepository.save(any())).thenReturn(employee);

        var result = employeeService.createEmployee(employeeRequest);

        assertThat(result).isEqualTo(employeeDTO);
        verify(eventPublisher, times(1)).publishActivity(any(), eq(ADDED));
    }

    @Test
    void getsAnEmployee() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Fo", organization);
        var employeeDTO = employee.toEmployeeDTO();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        var result = employeeService.getEmployee(employee.getId());

        assertThat(result).isEqualTo(employeeDTO);
    }

    @Test
    void throwsEntityNotFoundExceptionWhenEmployeeDoesNotExistById() {
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployee("non-existent"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Employee not found with id: non-existent");
    }

    @Test
    void updatesAnEmployeeAndPublishesItToTheEventPublisher() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Fo", organization);
        var expectedEmployee = dummyEmployee(employee.getId(), "Suzan", "Kan", organization);
        var employeeRequest = Fixture.dummyEmployeeRequest("Suzan", "Kan", organization);
        var employeeDTO = expectedEmployee.toEmployeeDTO();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(expectedEmployee)).thenReturn(expectedEmployee);

        var result = employeeService.updateEmployee(employee.getId(), employeeRequest);

        assertThat(result).isEqualTo(employeeDTO);
        verify(eventPublisher, times(1)).publishActivity(any(), eq(UPDATED));
    }

    @Test
    void updatesAnEmployeesAwardAndPublishesItToTheEventPublisher() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Fo", organization, 1);
        var expectedEmployee = Fixture.dummyEmployee(employee.getId(), "Alex", "Fo", organization, 2);
        var employeeDTO = expectedEmployee.toEmployeeDTO();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(expectedEmployee)).thenReturn(expectedEmployee);

        var result = employeeService.updateEmployeeAward(employee.getId());

        assertThat(result).isEqualTo(employeeDTO);
        verify(eventPublisher, times(1)).publishActivity(any(), eq(AWARDED));
        verify(eventPublisher, times(1)).publishAward(1);
    }

    @Test
    void deletesAnEmployeeAndPublishesItToTheEventPublisher() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Fo", organization);

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee.getId());

        employeeService.deleteEmployee(employee.getId());

        verify(employeeRepository, times(1)).delete(employee.getId());
        verify(eventPublisher, times(1)).publishActivity(any(), eq(REMOVED));
    }

    @Test
    void throwsEntityNotFoundExceptionWhenDeletingAnEmployeeThatDoesNotExist() {
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee("non-existent"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Employee not found with id: non-existent");
    }
}
