package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.model.Employee;

public record ActivityEvent(
    Employee employee,
    Status status
) {
}
