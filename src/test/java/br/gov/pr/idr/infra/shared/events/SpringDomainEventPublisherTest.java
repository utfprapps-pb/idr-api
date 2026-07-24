package br.gov.pr.idr.infra.shared.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringDomainEventPublisher")
class SpringDomainEventPublisherTest {

    @Mock ApplicationEventPublisher eventPublisher;
    @InjectMocks SpringDomainEventPublisher publisher;

    @Test
    @DisplayName("deve delegar publicação ao ApplicationEventPublisher")
    void shouldDelegateToApplicationEventPublisher() {
        final var event = new Object();

        publisher.publish(event);

        verify(eventPublisher).publishEvent(event);
    }
}
