package com.no.awards;

import com.no.awards.model.Employee;
import com.no.awards.model.Organization;
import com.no.awards.repository.ActivityRepository;
import com.no.awards.repository.EmployeeRepository;
import com.no.awards.repository.OrganizationRepository;
import com.no.awards.service.EmployeeService;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "data-loader.enabled", havingValue = "true")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


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
