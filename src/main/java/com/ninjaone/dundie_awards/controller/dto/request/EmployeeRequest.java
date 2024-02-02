package com.ninjaone.dundie_awards.controller.dto.request;

import com.ninjaone.dundie_awards.model.Organization;

public record EmployeeRequest(
        String firstName,
        String lastName,
        Organization organization
) {
}
