package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.event.Status;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import java.util.LinkedList;
import java.util.List;
import static java.util.UUID.randomUUID;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MessageBroker {

    private final List<Activity> messages = new LinkedList<>();

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
        messages.add(activity);
    }

    private void handleUpdate(Employee employee) {
        var activity = getActivity(String.format("Employee %s %s has been updated", employee.getFirstName(), employee.getLastName()));
        messages.add(activity);
    }

    private void handleAward(Employee employee) {
        var activity = getActivity(
            String.format("Employee %s %s has received a dundie award", employee.getFirstName(), employee.getLastName()));
        messages.add(activity);
    }

    private void handleRemove(Employee employee) {
        var activity = getActivity(String.format("Employee %s %s has been removed", employee.getFirstName(), employee.getLastName()));
        messages.add(activity);
    }

    private Activity getActivity(String event){
        return Activity
            .builder()
            .id(randomUUID().toString())
            .event(event)
            .build();
    }
}
