package com.ninjaone.dundie_awards.controller.dto.response;

import com.ninjaone.dundie_awards.model.Organization;
import lombok.Builder;

@Builder
public record EmployeeDTO(
        String id,
        String firstName,
        String lastName,
        Integer dundieAwards,
        Organization organization
) {
}
