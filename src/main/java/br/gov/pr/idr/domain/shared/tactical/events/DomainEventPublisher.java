package br.gov.pr.idr.domain.shared.tactical.events;

public interface DomainEventPublisher {
    void publish(Object event);
}
