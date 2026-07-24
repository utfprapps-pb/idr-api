package br.gov.pr.idr.domain.iam.user.exceptions;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;

import java.util.List;

public class UserException extends DomainException {

    public UserException(String message) {
        super(message, List.of());
    }
}
