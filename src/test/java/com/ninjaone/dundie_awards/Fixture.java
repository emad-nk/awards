package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.controller.dto.request.EmployeeRequestDTO;
import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

public class Fixture {

    public static Organization dummyOrganization(){
        return Organization.builder()
                .id(randomUUID().toString())
                .name("Accounting")
                .build();
    }

    public static Employee dummyEmployee(String firstName, String lastName, Organization organization) {
        return dummyEmployee(randomUUID().toString(), firstName, lastName, organization, 0);

    }

    public static Employee dummyEmployee(String firstName, String lastName, Organization organization, Integer awards) {
        return dummyEmployee(randomUUID().toString(), firstName, lastName, organization, awards);

    }

    public static Employee dummyEmployee(String id, String firstName, String lastName, Organization organization) {
        return dummyEmployee(id, firstName, lastName, organization, 0);
    }

    public static Employee dummyEmployee(String id, String firstName, String lastName, Organization organization, Integer awards) {
        return Employee.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .organization(organization)
            .dundieAwards(awards)
            .build();

    }

    public static EmployeeRequestDTO dummyEmployeeRequest(String firstName, String lastName, Organization organization) {
        return EmployeeRequestDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .organization(organization)
                .build();
    }

    public static EmployeeDTO dummyEmployeeDTO(String firstName, String lastName, Organization organization) {
        return EmployeeDTO.builder()
                .id(randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .organization(organization)
                .build();
    }

    public static Activity dummyActivity() {
        return Activity.builder()
            .id(randomUUID().toString())
            .occurredAt(now())
            .event("Some random test event")
            .build();
    }
}
