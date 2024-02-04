package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.model.Employee;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishActivity(Employee employee, Status status) {
        applicationEventPublisher.publishEvent(new ActivityEvent(employee, status));
    }

    public void publishAward(int numberOfAwards) {
        applicationEventPublisher.publishEvent(new AwardEvent(numberOfAwards));
    }

}
