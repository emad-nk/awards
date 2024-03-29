package com.no.awards;

import com.no.awards.event.Status;
import com.no.awards.model.Activity;
import com.no.awards.model.Employee;
import com.no.awards.property.MessageBrokerProperties;
import com.no.awards.repository.ActivityRepository;
import static java.time.Instant.now;
import java.util.LinkedList;
import java.util.List;
import static java.util.UUID.randomUUID;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The usage of message broker is unclear
 * To mimic that it's running in a different thread, and it's async it's called from a different thread via EmployeeEventListener
 */
@Component
@AllArgsConstructor
@Slf4j
@EnableConfigurationProperties(MessageBrokerProperties.class)
public class MessageBroker {

    @Getter
    private final List<Activity> messages = new LinkedList<>();
    private final ActivityRepository activityRepository;
    private final MessageBrokerProperties messageBrokerProperties;

    public void sendMessage(Employee employee, Status status) {
        switch (status) {
            case ADDED -> handleAdd(employee);
            case REMOVED -> handleRemove(employee);
            case UPDATED -> handleUpdate(employee);
            case AWARDED -> handleAward(employee);
        }
    }

    private void handleAdd(Employee employee) {
        var activity = getActivity(String.format("Employee %s %s has been added", employee.getFirstName(), employee.getLastName()));
        saveActivity(activity);
    }

    private void handleUpdate(Employee employee) {
        var activity = getActivity(String.format("Employee %s %s has been updated", employee.getFirstName(), employee.getLastName()));
        saveActivity(activity);
    }

    private void handleAward(Employee employee) {
        var activity = getActivity(
            String.format("Employee %s %s has received an award", employee.getFirstName(), employee.getLastName()));
        saveActivity(activity);
    }

    private void handleRemove(Employee employee) {
        var activity = getActivity(String.format("Employee %s %s has been removed", employee.getFirstName(), employee.getLastName()));
        saveActivity(activity);
    }

    private void saveActivity(Activity activity) {
        messages.add(activity);

        // Simulating messages are processed asynchronously and deleted afterward
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(messageBrokerProperties.delayMilliseconds());
                activityRepository.save(activity);
                messages.remove(activity);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Activity getActivity(String event) {
        return Activity
            .builder()
            .id(randomUUID().toString())
            .event(event)
            .occurredAt(now())
            .build();
    }
}
