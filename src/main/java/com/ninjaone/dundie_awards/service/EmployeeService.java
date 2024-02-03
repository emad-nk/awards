package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.controller.dto.request.EmployeeRequest;
import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import com.ninjaone.dundie_awards.event.EventPublisher;
import com.ninjaone.dundie_awards.event.Status;
import static com.ninjaone.dundie_awards.event.Status.ADDED;
import static com.ninjaone.dundie_awards.event.Status.AWARDED;
import static com.ninjaone.dundie_awards.event.Status.REMOVED;
import static com.ninjaone.dundie_awards.event.Status.UPDATED;
import com.ninjaone.dundie_awards.exception.EntityNotFoundException;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EventPublisher eventPublisher;

    public Page<EmployeeDTO> getAllEmployeesPaged(Pageable pageable) {
        var result = employeeRepository.getEmployees(pageable);
        return new PageImpl<>(
            result.map(Employee::toEmployeeDTO).stream().toList(),
            pageable,
            result.getTotalElements()
        );
    }

    public EmployeeDTO createEmployee(EmployeeRequest employeeRequest) {
        var employee = Employee.builder()
            .id(UUID.randomUUID().toString())
            .firstName(employeeRequest.firstName())
            .lastName(employeeRequest.lastName())
            .dundieAwards(0)
            .organization(employeeRequest.organization())
            .build();
        eventPublisher.publish(employee, ADDED);
        return employeeRepository.save(employee).toEmployeeDTO();
    }

    public EmployeeDTO getEmployee(String id) {
        return getEmployeeChecked(id).toEmployeeDTO();
    }

    public EmployeeDTO updateEmployee(String id, EmployeeRequest employeeRequest) {
        var employee = getEmployeeChecked(id);

        employee.setFirstName(employeeRequest.firstName());
        employee.setLastName(employeeRequest.lastName());
        employee.setOrganization(employeeRequest.organization());

        eventPublisher.publish(employee, UPDATED);
        return employeeRepository.save(employee).toEmployeeDTO();
    }

    public EmployeeDTO updateEmployeeAward(String id) {
        var employee = getEmployeeChecked(id);
        employee.setDundieAwards(employee.getDundieAwards() + 1);

        eventPublisher.publish(employee, AWARDED);
        return employeeRepository.save(employee).toEmployeeDTO();
    }

    public void deleteEmployee(String id) {
        var employee = getEmployeeChecked(id);
        employeeRepository.delete(employee);
        eventPublisher.publish(employee, REMOVED);
    }

    private Employee getEmployeeChecked(String id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id)
            );
    }
}
