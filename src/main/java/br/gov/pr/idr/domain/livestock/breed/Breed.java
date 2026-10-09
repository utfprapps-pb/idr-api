package br.gov.pr.idr.domain.livestock.breed;

import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

public class Breed extends AggregateRoot<BreedID> {

    private String breedName;
    private Long version;

    protected Breed(final BreedID id, final String breedName) {
        super(id);
        this.breedName = breedName;
    }

    public static Breed create(final String breedName) {
        final var breed = new Breed(BreedID.unique(), breedName);
        breed.selfValidate();
        return breed;
    }

    public static Breed with(final BreedID id, final String breedName) {
        return new Breed(id, breedName);
    }

    public Breed update(final String breedName) {
        this.breedName = breedName;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.breedName == null || this.breedName.isBlank()) {
            handler.append(DomainError.from("Nome da raça não pode ser vazio"));
        }
    }

    public String getBreedName() {
        return breedName;
    }

    public Long getVersion() {
        return version;
    }

    public Breed withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
