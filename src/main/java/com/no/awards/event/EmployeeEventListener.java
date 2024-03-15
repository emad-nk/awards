package com.no.awards.event;

import com.no.awards.MessageBroker;
import com.no.awards.service.AwardsCacheService;
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
