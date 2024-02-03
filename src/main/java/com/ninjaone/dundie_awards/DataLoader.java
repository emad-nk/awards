package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import com.ninjaone.dundie_awards.service.EmployeeService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;
    private final ActivityRepository activityRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmployeeService employeeService;

    @Override
    public void run(String... args) {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
        // uncomment to reseed data
         employeeRepository.deleteAll();
         organizationRepository.deleteAll();
         activityRepository.deleteAll();

        if (employeeRepository.count() == 0) {
            Organization organizationPikashu = new Organization(UUID.randomUUID().toString(), "Pikashu");
            organizationRepository.save(organizationPikashu);

            employeeRepository.save(new Employee(UUID.randomUUID().toString(), "John", "Doe", 0, organizationPikashu));
            employeeRepository.save(new Employee(UUID.randomUUID().toString(), "Jane", "Smith", 0, organizationPikashu));
            employeeRepository.save(new Employee(UUID.randomUUID().toString(), "Creed", "Braton", 0, organizationPikashu));

            Organization organizationSquanchy = new Organization(UUID.randomUUID().toString(), "Squanchy");
            organizationRepository.save(organizationSquanchy);

            employeeRepository.save(new Employee(UUID.randomUUID().toString(), "Michael", "Scott", 0, organizationSquanchy));
            var employee1 = employeeRepository.save(new Employee(UUID.randomUUID().toString(), "Dwight", "Schrute", 0, organizationSquanchy));
            var employee2 = employeeRepository.save(new Employee(UUID.randomUUID().toString(), "Jim", "Halpert", 0, organizationSquanchy));
            var employee3 = employeeRepository.save(new Employee(UUID.randomUUID().toString(), "Pam", "Beesley", 0, organizationSquanchy));

            employeeService.updateEmployeeAward(employee1.getId());
            employeeService.updateEmployeeAward(employee2.getId());
            employeeService.updateEmployeeAward(employee3.getId());
        }
    }
}
