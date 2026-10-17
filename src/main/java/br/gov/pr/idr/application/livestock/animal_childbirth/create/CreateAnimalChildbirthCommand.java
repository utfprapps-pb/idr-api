package br.gov.pr.idr.application.livestock.animal_childbirth.create;

import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalChildbirthCommand(
        UUID propertyId,
        UUID animalId,
        String username,
        LocalDate childbirthDate,
        ChildbirthGender gender,
        BigDecimal weight,
        ChildbirthCondition condition,
        UUID breedId,
        UUID calfId
) {
}
