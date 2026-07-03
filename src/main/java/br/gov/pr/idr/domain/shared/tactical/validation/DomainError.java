package br.gov.pr.idr.domain.shared.tactical.validation;

public record DomainError(String field, String message) {

    public static DomainError from(final String field, final String message) {
        return new DomainError(field, message);
    }

    public static DomainError from(final String message) {
        return from(null, message);
    }
}
