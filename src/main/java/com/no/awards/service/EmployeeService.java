package com.no.awards.service;

import com.no.awards.controller.dto.request.EmployeeRequestDTO;
import com.no.awards.controller.dto.response.EmployeeDTO;
import com.no.awards.event.EventPublisher;
import com.no.awards.exception.EntityNotFoundException;
import com.no.awards.model.Employee;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.configuration.RedisConfiguration;
import com.no.awards.event.Status;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EventPublisher eventPublisher;

    @Cacheable(RedisConfiguration.CacheNames.EMPLOYEES)
    public Page<EmployeeDTO> getAllEmployeesPaged(Pageable pageable) {
        var result = employeeRepository.getEmployees(pageable);
        return new PageImpl<>(
            result.map(Employee::toEmployeeDTO).stream().toList(),
            pageable,
            result.getTotalElements()
        );
    }

    public EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        var employee = Employee.builder()
            .id(UUID.randomUUID().toString())
            .firstName(employeeRequestDTO.firstName())
            .lastName(employeeRequestDTO.lastName())
            .awards(0)
            .organization(employeeRequestDTO.organization())
            .build();
        eventPublisher.publishActivity(employee, Status.ADDED);
        return employeeRepository.save(employee).toEmployeeDTO();
    }

    public EmployeeDTO getEmployee(String id) {
        return getEmployeeChecked(id).toEmployeeDTO();
    }

    @Transactional
    public EmployeeDTO updateEmployee(String id, EmployeeRequestDTO employeeRequestDTO) {
        var employee = getEmployeeChecked(id);

        employee.setFirstName(employeeRequestDTO.firstName());
        employee.setLastName(employeeRequestDTO.lastName());
        employee.setOrganization(employeeRequestDTO.organization());

        eventPublisher.publishActivity(employee, Status.UPDATED);
        return employeeRepository.save(employee).toEmployeeDTO();
    }

    @Transactional
    public EmployeeDTO updateEmployeeAward(String id) {
        var employee = getEmployeeChecked(id);
        employee.setAwards(employee.getAwards() + 1);

        eventPublisher.publishActivity(employee, Status.AWARDED);
        eventPublisher.publishAward(1);
        return employeeRepository.save(employee).toEmployeeDTO();
    }

    public void deleteEmployee(String id) {
        var employee = getEmployeeChecked(id);
        employeeRepository.delete(employee.getId());
        eventPublisher.publishActivity(employee, Status.REMOVED);
    }

    private Employee getEmployeeChecked(String id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id)
            );
    }
}
