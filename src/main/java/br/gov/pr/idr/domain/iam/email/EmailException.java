package br.gov.pr.idr.domain.iam.email;

import br.gov.pr.idr.domain.shared.tactical.exceptions.DomainException;

import java.util.List;

public class EmailException extends DomainException {

    public EmailException(String message) {
        super(message, List.of());
    }
}
