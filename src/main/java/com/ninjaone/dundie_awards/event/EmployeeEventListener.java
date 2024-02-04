package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.service.AwardsCacheService;
import com.ninjaone.dundie_awards.MessageBroker;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmployeeEventListener {

    private final MessageBroker messageBroker;
    private final AwardsCacheService awardsCacheService;

    @EventListener
    @Async
    public void processActivityEvent(ActivityEvent activityEvent) {
        messageBroker.sendMessage(activityEvent.employee(), activityEvent.status());
    }

    @EventListener
    @Async
    public void processAwardEvent(AwardEvent awardEvent) {
        awardsCacheService.addAwards((long) awardEvent.numberOfAwards());
    }

}
