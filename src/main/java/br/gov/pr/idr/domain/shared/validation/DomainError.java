package br.gov.pr.idr.domain.shared.validation;

public record DomainError(String message) {

    public static DomainError from(final String message) {
        return new DomainError(message);
    }
}
