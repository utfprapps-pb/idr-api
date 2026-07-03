package br.gov.pr.idr.domain.iam.user.exceptions;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;

import java.util.List;

public class CPFException extends DomainException {

    public CPFException(String message) {
        super(message, List.of(DomainError.from("cpf", message)));
    }
}
