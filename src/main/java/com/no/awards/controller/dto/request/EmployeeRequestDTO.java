package com.no.awards.controller.dto.request;

import com.no.awards.model.Organization;
import lombok.Builder;

@Builder
public record EmployeeRequestDTO(
    String firstName,
    String lastName,
    Organization organization
) {
}
