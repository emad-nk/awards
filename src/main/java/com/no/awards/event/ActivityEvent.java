package com.no.awards.event;

import com.no.awards.model.Employee;

public record ActivityEvent(
    Employee employee,
    Status status
) {
}
