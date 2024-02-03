package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.AwardsCache;
import static com.ninjaone.dundie_awards.Fixture.dummyEmployee;
import static com.ninjaone.dundie_awards.Fixture.dummyOrganization;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.controller.dto.response.EmployeeDTO;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private MessageBroker messageBroker;

    @Mock
    private AwardsCache awardsCache;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getsListOfEmployeesPaginated(){
        var organization = dummyOrganization();
        var employee1 = dummyEmployee("Alex", "Fo", organization);
        var employee2 = dummyEmployee("Sara", "Kalin", organization);
        var expectedResult = new PageImpl<>(
                Stream.of(employee1, employee2).toList(),
                PageRequest.of(0, 5),
                2
        );

        when(employeeRepository.getEmployees(PageRequest.of(0, 5))).thenReturn(expectedResult);

        var result = employeeService.getAllEmployeesPaged(PageRequest.of(0, 5));

        assertThat(result.stream().toList()).hasSize(2);
        assertThat(result.stream().map(EmployeeDTO::lastName).toList()).containsExactly("Fo", "Kalin");
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

}