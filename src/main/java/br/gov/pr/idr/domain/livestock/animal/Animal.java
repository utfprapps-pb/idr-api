package br.gov.pr.idr.domain.livestock.animal;

import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Animal do rebanho de uma propriedade. A propriedade é fixa desde a criação; raça e identificador são obrigatórios.
 * A mãe ({@code animalMotherId}) é opcional e, quando informada, deve ser outro animal da mesma propriedade.
 */
public class Animal extends AggregateRoot<AnimalID> {

    private final PropertyID propertyId;
    private AnimalID animalMotherId;
    private BreedID breedId;
    private String type;
    private String identifier;
    private String gender;
    private String bornCondition;
    private LocalDate bornDate;
    private Float bornWeight;
    private Float previousWeight;
    private Float currentWeight;
    private Float ecc;
    private Long version;

    protected Animal(final AnimalID id, final PropertyID propertyId, final AnimalID animalMotherId,
                      final BreedID breedId, final String type, final String identifier, final String gender,
                      final String bornCondition, final LocalDate bornDate, final Float bornWeight,
                      final Float previousWeight, final Float currentWeight, final Float ecc) {
        super(id);
        this.propertyId = propertyId;
        this.animalMotherId = animalMotherId;
        this.breedId = breedId;
        this.type = type;
        this.identifier = identifier;
        this.gender = gender;
        this.bornCondition = bornCondition;
        this.bornDate = bornDate;
        this.bornWeight = bornWeight;
        this.previousWeight = previousWeight;
        this.currentWeight = currentWeight;
        this.ecc = ecc;
    }

    public static Animal with(final AnimalID id, final PropertyID propertyId, final AnimalID animalMotherId,
                               final BreedID breedId, final String type, final String identifier,
                               final String gender, final String bornCondition, final LocalDate bornDate,
                               final Float bornWeight, final Float previousWeight, final Float currentWeight,
                               final Float ecc) {
        return new Animal(id, propertyId, animalMotherId, breedId, type, identifier, gender, bornCondition,
                bornDate, bornWeight, previousWeight, currentWeight, ecc);
    }

    public static Animal create(final PropertyID propertyId, final AnimalID animalMotherId, final BreedID breedId,
                                 final String type, final String identifier, final String gender,
                                 final String bornCondition, final LocalDate bornDate, final Float bornWeight,
                                 final Float previousWeight, final Float currentWeight, final Float ecc) {
        final var animal = new Animal(AnimalID.unique(), propertyId, animalMotherId, breedId, type, identifier,
                gender, bornCondition, bornDate, bornWeight, previousWeight, currentWeight, ecc);
        animal.selfValidate();
        return animal;
    }

    public Animal update(final AnimalID animalMotherId, final BreedID breedId, final String type,
                          final String identifier, final String gender, final String bornCondition,
                          final LocalDate bornDate, final Float bornWeight, final Float previousWeight,
                          final Float currentWeight, final Float ecc) {
        this.animalMotherId = animalMotherId;
        this.breedId = breedId;
        this.type = type;
        this.identifier = identifier;
        this.gender = gender;
        this.bornCondition = bornCondition;
        this.bornDate = bornDate;
        this.bornWeight = bornWeight;
        this.previousWeight = previousWeight;
        this.currentWeight = currentWeight;
        this.ecc = ecc;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.propertyId == null) {
            handler.append(DomainError.from("A propriedade do animal é obrigatória"));
        }
        if (this.breedId == null) {
            handler.append(DomainError.from("A raça do animal é obrigatória"));
        }
        if (this.identifier == null || this.identifier.isBlank()) {
            handler.append(DomainError.from("O identificador do animal é obrigatório"));
        }
        if (this.animalMotherId != null && this.animalMotherId.equals(getId())) {
            handler.append(DomainError.from("O animal não pode ser a própria mãe"));
        }
    }

    public PropertyID getPropertyId() {
        return propertyId;
    }

    public Optional<AnimalID> getAnimalMotherId() {
        return Optional.ofNullable(animalMotherId);
    }

    public BreedID getBreedId() {
        return breedId;
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getGender() {
        return gender;
    }

    public String getBornCondition() {
        return bornCondition;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public Float getBornWeight() {
        return bornWeight;
    }

    public Float getPreviousWeight() {
        return previousWeight;
    }

    public Float getCurrentWeight() {
        return currentWeight;
    }

    public Float getEcc() {
        return ecc;
    }

    public Long getVersion() {
        return version;
    }

    public Animal withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
