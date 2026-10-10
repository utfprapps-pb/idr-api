package br.gov.pr.idr.infra.livestock.animal_childbirth.models;

import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalChildbirthRequest(
        LocalDate childbirthDate,
        ChildbirthGender gender,
        BigDecimal weight,
        ChildbirthCondition condition,
        UUID breedId,
        UUID calfId
) {
}
