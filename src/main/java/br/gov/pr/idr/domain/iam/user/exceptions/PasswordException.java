package br.gov.pr.idr.domain.iam.user.exceptions;

import br.gov.pr.idr.domain.shared.exceptions.DomainException;

import java.util.List;

public class PasswordException extends DomainException {

    public PasswordException(String message) {
        super(message, List.of());
    }
}
