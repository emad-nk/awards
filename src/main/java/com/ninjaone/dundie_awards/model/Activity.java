package com.ninjaone.dundie_awards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
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

    private Instant occurredAt;

    private String event;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) && Objects.equals(occurredAt, activity.occurredAt) && Objects.equals(event, activity.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, occurredAt, event);
    }

    @Override
    public String toString() {
        return "Activity{" +
            "id='" + id + '\'' +
            ", occurredAt=" + occurredAt +
            ", event='" + event + '\'' +
            '}';
    }
}
