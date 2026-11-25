package br.gov.pr.idr.domain.livestock.animalsize;

import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

/** Porte, catálogo global (sem vínculo com propriedade). O nome é obrigatório. */
public class AnimalSize extends AggregateRoot<AnimalSizeID> {

    private String sizeName;
    private Long version;

    protected AnimalSize(final AnimalSizeID id, final String sizeName) {
        super(id);
        this.sizeName = sizeName;
    }

    public static AnimalSize create(final String sizeName) {
        final var animalSize = new AnimalSize(AnimalSizeID.unique(), sizeName);
        animalSize.selfValidate();
        return animalSize;
    }

    public static AnimalSize with(final AnimalSizeID id, final String sizeName) {
        return new AnimalSize(id, sizeName);
    }

    public AnimalSize update(final String sizeName) {
        this.sizeName = sizeName;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.sizeName == null || this.sizeName.isBlank()) {
            handler.append(DomainError.from("Nome do porte não pode ser vazio"));
        }
    }

    public String getSizeName() {
        return sizeName;
    }

    public Long getVersion() {
        return version;
    }

    public AnimalSize withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
