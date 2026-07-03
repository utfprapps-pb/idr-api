package br.gov.pr.idr.domain.shared.tactical.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final DomainError domainError);

    <T> T validate(Validation<T> aValidation);

    List<DomainError> getErrors();

    interface Validation<T> {
        T validate();
    }

}
