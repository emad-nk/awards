package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class EventListener {

    private MessageBroker messageBroker;
    private AwardsCache awardsCache;

    @TransactionalEventListener
    @Async
    public void processEvent(Event event) {
        publishToMessageBroker(event);
        updateAwardsCache(event);
    }

    private void publishToMessageBroker(Event event) {
        messageBroker.sendMessage(event.employee(), event.status());
    }

    private void updateAwardsCache(Event event) {
        awardsCache.addOneAward();
    }

}
