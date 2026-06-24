package br.gov.pr.idr.domain.shared.events;

public interface DomainEventPublisher {
    void publish(Object event);
}
