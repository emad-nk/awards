package com.ninjaone.dundie_awards.event;

import com.ninjaone.dundie_awards.MessageBroker;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BrokerEventListener {

    private MessageBroker messageBroker;

    @EventListener
    @Async
    public void processEvent(Event event) {
        messageBroker.sendMessage(event.employee(), event.status());
    }

}
