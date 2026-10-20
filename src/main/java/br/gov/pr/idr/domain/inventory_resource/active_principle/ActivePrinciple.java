package br.gov.pr.idr.domain.inventory_resource.active_principle;

import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

/** Princípio ativo de produtos, catálogo global. O nome é obrigatório. */
public class ActivePrinciple extends AggregateRoot<ActivePrincipleID> {

    private String name;
    private Long version;

    protected ActivePrinciple(final ActivePrincipleID id, final String name) {
        super(id);
        this.name = name;
    }

    public static ActivePrinciple create(final String name) {
        final var principle = new ActivePrinciple(ActivePrincipleID.unique(), name);
        principle.selfValidate();
        return principle;
    }

    public static ActivePrinciple with(final ActivePrincipleID id, final String name) {
        return new ActivePrinciple(id, name);
    }

    public ActivePrinciple update(final String name) {
        this.name = name;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("Nome do princípio ativo não pode ser vazio"));
        }
    }

    public String getName() {
        return name;
    }

    public Long getVersion() {
        return version;
    }

    public ActivePrinciple withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
