package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.controller.dto.request.EmployeeRequest;
import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import java.util.UUID;
import static java.util.UUID.randomUUID;

public class Fixture {

    public static Organization dummyOrganization(){
        return Organization.builder()
                .id(randomUUID().toString())
                .name("Accounting")
                .build();
    }

    public static Employee dummyEmployee(String id, String firstName, String lastName, Organization organization) {
        return Employee.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .organization(organization)
            .dundieAwards(0)
            .build();
    }

    public static Employee dummyEmployee(String firstName, String lastName, Organization organization) {
        return dummyEmployee(randomUUID().toString(), firstName, lastName, organization);

    }

    public static EmployeeRequest dummyEmployeeRequest(String firstName, String lastName, Organization organization) {
        return EmployeeRequest.builder()
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
}
