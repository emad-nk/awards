package com.ninjaone.dundie_awards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.dundie_awards.CustomPage;
import static com.ninjaone.dundie_awards.Fixture.dummyEmployee;
import static com.ninjaone.dundie_awards.Fixture.dummyEmployeeRequest;
import static com.ninjaone.dundie_awards.Fixture.dummyOrganization;
import com.ninjaone.dundie_awards.IntegrationTestParent;
import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import java.util.ArrayList;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeControllerIT extends IntegrationTestParent {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private final String EMPLOYEE_URI = "/api/v1/employees";

    @Test
    void createsAnEmployee() {
        var organization = dummyOrganization();
        var employeeRequest = dummyEmployeeRequest("Alex", "Pascal", organization);

        organizationRepository.save(organization);

        EmployeeDTO employeeDTO = given()
                .contentType(JSON)
                .when()
                .body(employeeRequest)
                .post(EMPLOYEE_URI)
                .then()
                .log().ifValidationFails()
                .statusCode(SC_CREATED)
                .extract()
                .body()
                .as(EmployeeDTO.class);

        assertThat(employeeDTO.id()).isNotNull();
        assertThat(employeeDTO.firstName()).isEqualTo(employeeRequest.firstName());
        assertThat(employeeDTO.lastName()).isEqualTo(employeeRequest.lastName());
        assertThat(employeeDTO.dundieAwards()).isEqualTo(0);
    }

    @Test
    void getsAllEmployeesByPagingAndSortingThem() throws JsonProcessingException {
        var organization = dummyOrganization();
        var employee1 = dummyEmployee("Alex", "Pascal", organization);
        var employee2 = dummyEmployee("John", "Doe", organization);
        var employee3 = dummyEmployee("James", "Bond", organization);


        organizationRepository.save(organization);
        employeeRepository.saveAll(new ArrayList<>() {{
            add(employee1);
            add(employee2);
            add(employee3);
        }});

        var page1 = getEmployees(0, 2, "last_name");
        var page2 = getEmployees(1, 2, "last_name");


        assertThat(page1.content()).hasSize(2);
        assertThat(page1.content().stream().map(EmployeeDTO::lastName).toList()).containsExactly("Bond", "Doe");
        assertThat(page2.content()).hasSize(1);
        assertThat(page2.content().stream().map(EmployeeDTO::lastName).toList()).containsExactly("Pascal");
    }

    @Test
    void getsAnEmployeeById() {
        var organization = dummyOrganization();
        var employee = dummyEmployee("Alex", "Pascal", organization);
        var expectedEmployeeDTO = employee.toEmployeeDTO();

        organizationRepository.save(organization);
        employeeRepository.save(employee);

        EmployeeDTO employeeDTO = given()
                .contentType(JSON)
                .when()
                .pathParam("id", employee.getId())
                .get(EMPLOYEE_URI + "/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .body()
                .as(EmployeeDTO.class);

        assertThat(employeeDTO).isEqualTo(expectedEmployeeDTO);
    }

    @Test
    void deletedAnEmployeeById() {
        var organization = dummyOrganization();
        var employee = dummyEmployee("Alex", "Pascal", organization);

        organizationRepository.save(organization);
        employeeRepository.save(employee);

        given()
                .contentType(JSON)
                .when()
                .pathParam("id", employee.getId())
                .delete(EMPLOYEE_URI + "/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(SC_NO_CONTENT);

        assertThat(employeeRepository.findAll()).isEmpty();
    }

    @Test
    void throwsNotFoundExceptionWhenDeletingAnEmployeeThatDoesNotExist() {
        given()
                .contentType(JSON)
                .when()
                .pathParam("id", "non-existent")
                .delete(EMPLOYEE_URI + "/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    void updatesAnEmployee() {
        var organization = dummyOrganization();
        var employee = dummyEmployee("Alex", "Pascal", organization);
        var employeeRequest = dummyEmployeeRequest("Alex", "Doe", organization);

        organizationRepository.save(organization);
        employeeRepository.save(employee);

        EmployeeDTO employeeDTO = given()
                .contentType(JSON)
                .when()
                .pathParam("id", employee.getId())
                .body(employeeRequest)
                .put(EMPLOYEE_URI + "/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .body()
                .as(EmployeeDTO.class);

        assertThat(employeeDTO.id()).isEqualTo(employee.getId());
        assertThat(employeeDTO.lastName()).isEqualTo(employeeRequest.lastName());
    }

    @Test
    void throwsNotFoundExceptionWhenUpdatingAnEmployeeThatDoesNotExist() {
        var organization = dummyOrganization();
        var employeeRequest = dummyEmployeeRequest("Alex", "Doe", organization);
        given()
                .contentType(JSON)
                .when()
                .pathParam("id", "non-existent")
                .body(employeeRequest)
                .put(EMPLOYEE_URI + "/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(SC_NOT_FOUND);
    }

    private CustomPage<EmployeeDTO> getEmployees(int page, int size, String sort) throws JsonProcessingException {
        var response = given()
                .contentType(JSON)
                .param("sort", sort)
                .param("page", page)
                .param("size", size)
                .when()
                .get(EMPLOYEE_URI)
                .then()
                .log().ifValidationFails()
                .statusCode(SC_OK)
                .extract()
                .body()
                .asString();

        return objectMapper.readValue(response, new TypeReference<>() {
        });
    }
}