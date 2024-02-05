package com.ninjaone.dundie_awards.model;

import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
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

    private String firstName;

    private String lastName;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
            Objects.equals(firstName, employee.firstName) &&
            Objects.equals(lastName, employee.lastName) &&
            Objects.equals(dundieAwards, employee.dundieAwards) &&
            Objects.equals(organization, employee.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dundieAwards, organization);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id='" + id + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dundieAwards=" + dundieAwards +
            ", organization=" + organization +
            '}';
    }
}
