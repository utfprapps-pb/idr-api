package br.gov.pr.idr.infra.shared;

public record FieldError(String field, String message) {

    public static FieldError from(final String field, final String message) {
        return new FieldError(field, message);
    }
}
