package br.gov.pr.idr.infra.livestock.animal_childbirth.models.update;

import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthOutput;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalChildbirthResponse(
        UUID id,
        UUID animalId,
        LocalDate childbirthDate,
        ChildbirthGender gender,
        BigDecimal weight,
        ChildbirthCondition condition,
        UUID breedId,
        UUID calfId
) {

    public static UpdateAnimalChildbirthResponse from(final UpdateAnimalChildbirthOutput output) {
        return new UpdateAnimalChildbirthResponse(
                output.id(),
                output.animalId(),
                output.childbirthDate(),
                output.gender(),
                output.weight(),
                output.condition(),
                output.breedId(),
                output.calfId()
        );
    }
}
