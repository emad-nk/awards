package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Activity {

    @Id
    private String id;

    @Column(name = "occurred_at")
    private Instant occurredAt;

    @Column(name = "event")
    private String event;

}
