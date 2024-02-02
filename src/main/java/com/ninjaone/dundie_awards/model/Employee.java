package com.ninjaone.dundie_awards.model;

import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dundie_awards")
    Integer dundieAwards;

    @ManyToOne
    private Organization organization;

    public EmployeeDTO toEmployeeDTO() {
        return EmployeeDTO.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .dundieAwards(this.dundieAwards)
                .organization(this.organization)
                .build();
    }
}