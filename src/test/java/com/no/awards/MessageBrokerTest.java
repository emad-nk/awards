package com.no.awards;

import static com.no.awards.Fixture.dummyEmployee;
import com.no.awards.event.Status;
import com.no.awards.property.MessageBrokerProperties;
import com.no.awards.repository.ActivityRepository;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageBrokerTest {

    private ActivityRepository activityRepository;

    private MessageBroker messageBroker;

    @BeforeEach
    void setup() {
        activityRepository = mock(ActivityRepository.class);
        MessageBrokerProperties messageBrokerProperties = new MessageBrokerProperties((long) 50);
        messageBroker = new MessageBroker(activityRepository, messageBrokerProperties);

        when(activityRepository.save(any())).thenReturn(Fixture.dummyActivity());
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    void sendsDifferentMessagesToTheMessageBrokerAndSaveItToTheActivityTableAfterProcessingAndRemovesTheMessageFromTheMessageBrokerMessages(Status status) {
        var organization = Fixture.dummyOrganization();
        var employee = Fixture.dummyEmployee("John", "Paul", organization);

        messageBroker.sendMessage(employee, status);

        assertThat(messageBroker.getMessages()).hasSize(1);
        await().atMost(3, SECONDS).untilAsserted(
            () -> {
                verify(activityRepository, times(1)).save(any());
                assertThat(messageBroker.getMessages()).isEmpty();
            }
        );
    }
}
