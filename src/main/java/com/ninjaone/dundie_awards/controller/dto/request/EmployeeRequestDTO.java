package com.ninjaone.dundie_awards.controller.dto.request;

import com.ninjaone.dundie_awards.model.Organization;
import lombok.Builder;

@Builder
public record EmployeeRequestDTO(
        String firstName,
        String lastName,
        Organization organization
) {
}
