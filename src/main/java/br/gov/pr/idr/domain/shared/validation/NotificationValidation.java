package br.gov.pr.idr.domain.shared.validation;

import br.gov.pr.idr.domain.shared.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;

public class NotificationValidation implements ValidationHandler {

    private final List<DomainError> errors;

    NotificationValidation(List<DomainError> errors) {
        this.errors = errors;
    }

    public static NotificationValidation create() {
        return new NotificationValidation(new ArrayList<>());
    }

    @Override
    public ValidationHandler append(final DomainError domainError) {
        this.errors.add(domainError);
        return this;
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (RuntimeException t) {
            this.errors.add(DomainError.from("error", t.getMessage()));
        }
        return null;
    }

    @Override
    public List<DomainError> getErrors() {
        return this.errors;
    }

    public boolean hasError() {
        return !this.errors.isEmpty();
    }
}
