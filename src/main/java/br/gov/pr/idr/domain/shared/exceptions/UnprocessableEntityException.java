package br.gov.pr.idr.domain.shared.exceptions;

import java.util.List;

public class UnprocessableEntityException extends DomainException {

    public UnprocessableEntityException(final String context) {
        super(context, List.of());
    }
}
