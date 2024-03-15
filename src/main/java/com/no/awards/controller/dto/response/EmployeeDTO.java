package com.no.awards.controller.dto.response;

import com.no.awards.model.Organization;
import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;

@Builder
public record EmployeeDTO(
    String id,
    String firstName,
    String lastName,
    Integer awards,
    Organization organization
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3387516993334229948L;
}
