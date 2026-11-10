package br.gov.pr.idr.application.livestock.animal_childbirth.update;

import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalChildbirthOutput(
        UUID id,
        UUID animalId,
        LocalDate childbirthDate,
        ChildbirthGender gender,
        BigDecimal weight,
        ChildbirthCondition condition,
        UUID breedId,
        UUID calfId
) {

    public static UpdateAnimalChildbirthOutput from(final AnimalChildbirth entity) {
        return new UpdateAnimalChildbirthOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getChildbirthDate(),
                entity.getGender(),
                entity.getWeight(),
                entity.getCondition(),
                entity.getBreedId().id(),
                entity.getCalfId().map(id -> id.id()).orElse(null)
        );
    }
}
