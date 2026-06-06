package br.gov.pr.idr.domain.shared;

import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

import java.util.Objects;

/**
 * Uma entidade é definada pela definição de determinadas caracteristicas:
 *  - Uma entidade deve ter um identificador único
 *  - Controle de estado e seus comportamentos são designados a partir de um agregado
 */
public abstract class Entity<I extends Identifier> {

    private final I id;

    public abstract void validate(ValidationHandler handler);

    protected Entity(I id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        this.id = id;
    }

    public I getId() {return this.id;}

    protected void selfValidate() {
        final NotificationValidation handler = NotificationValidation.create();
        this.validate(handler);
        if (handler.hasError()) {
            throw new NotificationException("Falha a executar a ação: ", handler);
        }
    }
}
