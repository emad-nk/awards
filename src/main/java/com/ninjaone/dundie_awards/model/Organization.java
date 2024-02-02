package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Organization {

  @Id
  private String id;

  @Column(name = "name")
  private String name;
}
