package com.no.awards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.no.awards.CustomPage;
import static com.no.awards.Fixture.dummyEmployee;
import com.no.awards.IntegrationTestParent;
import com.no.awards.controller.dto.response.EmployeeDTO;
import com.no.awards.repository.ActivityRepository;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.repository.OrganizationRepository;
import com.no.awards.service.AwardsCacheService;
import com.no.awards.Fixture;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import java.util.ArrayList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeControllerIT extends IntegrationTestParent {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AwardsCacheService awardsCacheService;
    @Autowired
    private ActivityRepository activityRepository;

    private final String EMPLOYEE_URI = "/api/v1/employees";

    @Test
    void createsAnEmployeeAndAddsActivityToTheActivityTable() {
        var organization = Fixture.dummyOrganization();
        var employeeRequest = Fixture.dummyEmployeeRequest("Alex", "Pascal", organization);

        organizationRepository.save(organization);

        assertThat(activityRepository.findAll()).isEmpty();

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
        assertThat(employeeDTO.awards()).isEqualTo(0);
        assertThat(awardsCacheService.getTotalAwards()).isEqualTo(0);

        await().atMost(5, SECONDS).untilAsserted(
            () -> assertThat(activityRepository.findAll()).hasSize(1)
        );
    }

    @Test
    void getsAllEmployeesByPagingAndSortingThem() throws JsonProcessingException {
        var organization = Fixture.dummyOrganization();
        var employee1 = Fixture.dummyEmployee("Alex", "Pascal", organization);
        var employee2 = Fixture.dummyEmployee("John", "Doe", organization);
        var employee3 = Fixture.dummyEmployee("James", "Bond", organization);


        organizationRepository.save(organization);
        employeeRepository.saveAll(new ArrayList<>() {{
            add(employee1);
            add(employee2);
            add(employee3);
        }});

        var page1 = getEmployees(0, 2, "last_name");
        var page2 = getEmployees(1, 2, "last_name");


        Assertions.assertThat(page1.content()).hasSize(2);
        Assertions.assertThat(page1.content().stream().map(EmployeeDTO::lastName).toList()).containsExactly("Bond", "Doe");
        Assertions.assertThat(page2.content()).hasSize(1);
        Assertions.assertThat(page2.content().stream().map(EmployeeDTO::lastName).toList()).containsExactly("Pascal");
    }

    @Test
    void getsAnEmployeeById() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Pascal", organization);
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
    void deletedAnEmployeeByIdAndAddsTheActivityToTheActivityTable() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Pascal", organization);

        organizationRepository.save(organization);
        employeeRepository.save(employee);

        assertThat(activityRepository.findAll()).isEmpty();

        given()
                .contentType(JSON)
                .when()
                .pathParam("id", employee.getId())
                .delete(EMPLOYEE_URI + "/{id}")
                .then()
                .log().ifValidationFails()
                .statusCode(SC_NO_CONTENT);

        Assertions.assertThat(employeeRepository.findAll()).isEmpty();
        await().atMost(5, SECONDS).untilAsserted(
            () -> assertThat(activityRepository.findAll()).hasSize(1)
        );
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
    void updatesAnEmployeeAndAddsTheActivityToTheActivityTable() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Pascal", organization);
        var employeeRequest = Fixture.dummyEmployeeRequest("John", "Doe", organization);
        var expectedEmployeeDTO = Fixture.dummyEmployee(employee.getId(), employeeRequest.firstName(), employeeRequest.lastName(), organization).toEmployeeDTO();

        organizationRepository.save(organization);
        employeeRepository.save(employee);

        assertThat(activityRepository.findAll()).isEmpty();

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

        assertThat(employeeDTO).isEqualTo(expectedEmployeeDTO);
        await().atMost(5, SECONDS).untilAsserted(
            () -> assertThat(activityRepository.findAll()).hasSize(1)
        );
    }

    @Test
    void updatesAnEmployeeAwardAndItsCacheAndAddsTheActivityToTheActivityTable() {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("Alex", "Pascal", organization, 5);
        var expectedEmployeeDTO = Fixture.dummyEmployee(employee.getId(), employee.getFirstName(), employee.getLastName(), organization, 6).toEmployeeDTO();


        organizationRepository.save(organization);
        employeeRepository.save(employee);

        assertThat(activityRepository.findAll()).isEmpty();
        assertThat(awardsCacheService.getTotalAwards()).isEqualTo(5);

        EmployeeDTO employeeDTO = given()
            .contentType(JSON)
            .when()
            .pathParam("id", employee.getId())
            .put(EMPLOYEE_URI + "/{id}/awards")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_OK)
            .extract()
            .body()
            .as(EmployeeDTO.class);

        assertThat(employeeDTO).isEqualTo(expectedEmployeeDTO);

        await().atMost(5, SECONDS).untilAsserted(
            () -> {
                assertThat(activityRepository.findAll()).hasSize(1);
                assertThat(awardsCacheService.getTotalAwards()).isEqualTo(6);
            }
        );
    }

    @Test
    void throwsNotFoundExceptionWhenUpdatingAnEmployeeThatDoesNotExist() {
        var organization = Fixture.dummyOrganization();
        var employeeRequest = Fixture.dummyEmployeeRequest("Alex", "Doe", organization);
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
