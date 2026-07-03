package br.gov.pr.idr.domain.shared.tactical;

/**
 * Utilize um agregado quando:
 *  - Existe um Identificador único
 *  - O própprio agregado realiza o controle do seu estado
 *  - Agregados podem manipular estados de outras entidades
 *  - Relacionamento entre agregados é realizada via Identificador
 */
public abstract class AggregateRoot<I extends Identifier> extends Entity<I> {
    protected AggregateRoot(final I id) {
        super(id);
    }
}
