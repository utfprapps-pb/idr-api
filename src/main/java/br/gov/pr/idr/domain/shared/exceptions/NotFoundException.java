package br.gov.pr.idr.domain.shared.exceptions;

import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.Identifier;
import br.gov.pr.idr.domain.shared.validation.DomainError;

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
}
