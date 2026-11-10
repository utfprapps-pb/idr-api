package br.gov.pr.idr.domain.shared.tactical.exceptions;

import java.util.List;

public class ForbiddenException extends DomainException {

    private ForbiddenException(final String message) {
        super(message, List.of());
    }

    public static ForbiddenException with(final String message) {
        return new ForbiddenException(message);
    }
}
