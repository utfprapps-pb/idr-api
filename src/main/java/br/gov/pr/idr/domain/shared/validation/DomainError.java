package br.gov.pr.idr.domain.shared.validation;

public record DomainError(String field, String message) {

    public static DomainError from(final String field, final String message) {
        return new DomainError(field, message);
    }
}
