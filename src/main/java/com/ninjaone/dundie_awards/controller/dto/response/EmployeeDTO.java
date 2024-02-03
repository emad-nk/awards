package com.ninjaone.dundie_awards.controller.dto.response;

import com.ninjaone.dundie_awards.model.Organization;
import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;

@Builder
public record EmployeeDTO(
    String id,
    String firstName,
    String lastName,
    Integer dundieAwards,
    Organization organization
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3387516993334229948L;
}
