package br.gov.pr.idr.domain.shared.tactical.exceptions;

import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.Identifier;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;

import java.util.List;

public class NotFoundException extends DomainException {

    NotFoundException(final String context, List<DomainError> errors) {
        super(context, errors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> clazz,
                                         final Identifier id) {
        final var mesage = "%s com o ID %s não encontrada".formatted(clazz.getSimpleName(), id.id());
       return new NotFoundException(mesage, List.of());
    }

    public static NotFoundException with(final String message) {
        return new NotFoundException(message, List.of());
    }
}
