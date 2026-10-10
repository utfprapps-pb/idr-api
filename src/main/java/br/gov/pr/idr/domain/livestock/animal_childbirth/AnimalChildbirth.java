package br.gov.pr.idr.domain.livestock.animal_childbirth;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * A childbirth of the mother {@code animalId}. The calf may optionally be linked to an animal of the same
 * property ({@code calfId}); it stays empty when the calf is not formally part of the herd.
 */
public class AnimalChildbirth extends AggregateRoot<AnimalChildbirthID> {

    private final AnimalID animalId;
    private LocalDate childbirthDate;
    private ChildbirthGender gender;
    private BigDecimal weight;
    private ChildbirthCondition condition;
    private BreedID breedId;
    private AnimalID calfId;
    private Long version;

    protected AnimalChildbirth(final AnimalChildbirthID id, final AnimalID animalId,
                               final LocalDate childbirthDate, final ChildbirthGender gender,
                               final BigDecimal weight, final ChildbirthCondition condition,
                               final BreedID breedId, final AnimalID calfId) {
        super(id);
        this.animalId = animalId;
        this.childbirthDate = childbirthDate;
        this.gender = gender;
        this.weight = weight;
        this.condition = condition;
        this.breedId = breedId;
        this.calfId = calfId;
    }

    public static AnimalChildbirth with(final AnimalChildbirthID id, final AnimalID animalId,
                                        final LocalDate childbirthDate, final ChildbirthGender gender,
                                        final BigDecimal weight, final ChildbirthCondition condition,
                                        final BreedID breedId, final AnimalID calfId) {
        return new AnimalChildbirth(id, animalId, childbirthDate, gender, weight, condition, breedId, calfId);
    }

    public static AnimalChildbirth create(final AnimalID animalId, final LocalDate childbirthDate,
                                          final ChildbirthGender gender, final BigDecimal weight,
                                          final ChildbirthCondition condition, final BreedID breedId,
                                          final AnimalID calfId) {
        final var animalChildbirth = new AnimalChildbirth(AnimalChildbirthID.unique(), animalId, childbirthDate,
                gender, weight, condition, breedId, calfId);
        animalChildbirth.selfValidate();
        return animalChildbirth;
    }

    public AnimalChildbirth update(final LocalDate childbirthDate, final ChildbirthGender gender,
                                   final BigDecimal weight, final ChildbirthCondition condition,
                                   final BreedID breedId, final AnimalID calfId) {
        this.childbirthDate = childbirthDate;
        this.gender = gender;
        this.weight = weight;
        this.condition = condition;
        this.breedId = breedId;
        this.calfId = calfId;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.animalId == null) {
            handler.append(DomainError.from("O animal do parto é obrigatório"));
        }
        if (this.breedId == null) {
            handler.append(DomainError.from("A raça do bezerro é obrigatória"));
        }
        if (this.calfId != null && this.calfId.equals(this.animalId)) {
            handler.append(DomainError.from("O bezerro não pode ser a própria mãe"));
        }
    }

    public AnimalID getAnimalId() {
        return animalId;
    }

    public LocalDate getChildbirthDate() {
        return childbirthDate;
    }

    public ChildbirthGender getGender() {
        return gender;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public ChildbirthCondition getCondition() {
        return condition;
    }

    public BreedID getBreedId() {
        return breedId;
    }

    public Optional<AnimalID> getCalfId() {
        return Optional.ofNullable(calfId);
    }

    public Long getVersion() {
        return version;
    }

    public AnimalChildbirth withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
