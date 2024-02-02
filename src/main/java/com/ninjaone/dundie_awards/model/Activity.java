package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@Getter
public class Activity {

    @Id
    private String id;

    @Column(name = "occurred_at")
    private Instant occurredAt;

    @Column(name = "event")
    private String event;


    public Activity(Instant occurredAt, String event) {
        super();
        this.occurredAt = occurredAt;
        this.event = event;
    }
}
