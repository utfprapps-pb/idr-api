package br.gov.pr.idr.domain.shared.exceptions;

import br.gov.pr.idr.domain.shared.validation.DomainError;

import java.util.List;
import java.util.stream.Collectors;

public class DomainException extends RuntimeException {

    private final String context;
    protected final transient List<DomainError> errors;

    protected DomainException(String context, List<DomainError> errors) {
        super(formatMessage(context, errors));
        this.context = context;
        this.errors = errors;
    }

    public static DomainException from(final String context) {
        return new DomainException(context, List.of());
    }

    public String getContext() {
        return context;
    }

    public List<DomainError> getErrors() {
        return errors;
    }

    private static String formatMessage(String context, List<DomainError> errors) {
        if (errors == null || errors.isEmpty()) {
            return context;
        }
        if (errors.size() == 1 && context != null && context.equals(errors.getFirst().message())) {
            return context;
        }
        return "%s: %s".formatted(context, errors.stream()
                .map(DomainError::message)
                .collect(Collectors.joining(", ")));
    }
}
