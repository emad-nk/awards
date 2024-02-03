package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.MessageBroker;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class EventListener {

    private MessageBroker messageBroker;

    @TransactionalEventListener
    @Async
    public void processEvent(Event event) {
        messageBroker.sendMessage(event.employee(), event.status());
    }

}
