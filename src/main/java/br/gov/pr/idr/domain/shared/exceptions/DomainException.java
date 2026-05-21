package br.gov.pr.idr.domain.shared.exceptions;

import br.gov.pr.idr.domain.shared.validation.DomainError;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DomainException extends RuntimeException {

    protected final transient List<DomainError> errors;

    protected DomainException(String message, List<DomainError> errors) {
        super(formatMessage(message, errors));
        this.errors = errors;
    }

    public List<DomainError> getErrors() {
        return errors;
    }

    private static String formatMessage(String message, List<DomainError> errors) {
        if (errors == null || errors.isEmpty()) {
            return message;
        }
        final String errorsMessage = errors.stream()
                .map(DomainError::message)
                .collect(Collectors.joining(", "));

        return "%s: %s".formatted(message, errorsMessage);
    }
}
