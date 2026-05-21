package br.gov.pr.idr.domain.shared;

import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

/**
 * Uma entidade é definada pela definição de determinadas caracteristicas:
 *  - Uma entidade deve ter um identificador único
 *  - Uma entidade não tem seu controle de estado independente
 *  - Todos os seus comportamentos são designados a partir de um agregado
 */
public abstract class Entity<I extends Identifier> {

    private final I id;

    public abstract void validate(ValidationHandler handler);

    protected Entity(I id) {
        this.id = id;
    }

    public I getId() {return this.id;}
}
