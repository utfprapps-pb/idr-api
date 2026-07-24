package br.gov.pr.idr.domain.iam.user.events;

public record PasswordResetEvent(String email, String name) {
}
