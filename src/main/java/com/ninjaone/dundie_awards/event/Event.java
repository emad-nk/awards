package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.model.Employee;

public record Event(
    Employee employee,
    Status status
) {
}
