package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.controller.dto.request.EmployeeRequest;
import com.ninjaone.dundie_awards.exception.EntityNotFoundException;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ActivityRepository activityRepository;
    private final MessageBroker messageBroker;
    private final AwardsCache awardsCache;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(
            final EmployeeRepository employeeRepository,
            final ActivityRepository activityRepository,
            final MessageBroker messageBroker,
            final AwardsCache awardsCache
    ) {
        this.employeeRepository = employeeRepository;
        this.activityRepository = activityRepository;
        this.messageBroker = messageBroker;
        this.awardsCache = awardsCache;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(EmployeeRequest employeeRequest) {
        var employee = new Employee(
                employeeRequest.firstName(),
                employeeRequest.lastName(),
                employeeRequest.organization()
        );
        return  employeeRepository.save(employee);
    }

    public Employee getEmployee(Long id) {
        return getEmployeeChecked(id);
    }

    public Employee updateEmployee(Long id, EmployeeRequest employeeRequest) {
        var employee = getEmployeeChecked(id);

        employee.setFirstName(employeeRequest.firstName());
        employee.setLastName(employeeRequest.lastName());
        employee.setOrganization(employeeRequest.organization());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        var employee = getEmployeeChecked(id);
        employeeRepository.delete(employee);
    }

    private Employee getEmployeeChecked(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id:" + id)
                );
    }
}
