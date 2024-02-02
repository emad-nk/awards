package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.controller.dto.request.EmployeeRequest;
import com.ninjaone.dundie_awards.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

import com.ninjaone.dundie_awards.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private ActivityRepository activityRepository;
//
//    @Autowired
//    private MessageBroker messageBroker;
//
//    @Autowired
//    private AwardsCache awardsCache;

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    @Operation(summary = "Gets all the employees")
    @ResponseStatus(code = OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful")
    })
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/employees")
    @Operation(summary = "Saves a new employee in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Could save")
    })
    @ResponseStatus(code = CREATED)
    public Employee createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest);
    }

    @GetMapping("/employees/{id}")
    @Operation(summary = "Gets an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @ResponseStatus(code = OK)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    @PutMapping("/employees/{id}")
    @Operation(summary = "Updates an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @ResponseStatus(code = OK)
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeRequest));
    }

    @DeleteMapping("/employees/{id}")
    @Operation(summary = "Deletes an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content, succesfully deleted"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @ResponseStatus(code = NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}