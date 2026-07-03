package br.gov.pr.idr.domain.iam.user.exceptions;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;

import java.util.List;

public class PasswordException extends DomainException {

    public PasswordException(String field, String message) {
        super(message, List.of(DomainError.from(field, message)));
    }
}
