package br.gov.pr.idr.domain.iam.user.events;

public record UserCreatedEvent(String email, String name) {
}
