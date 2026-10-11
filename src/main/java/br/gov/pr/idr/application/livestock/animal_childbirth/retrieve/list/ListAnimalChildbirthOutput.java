package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list;

import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/** {@code breedName}/{@code calfIdentifier} são para exibição; mesmos campos da saída do GET. */
public record ListAnimalChildbirthOutput(
        UUID id,
        UUID animalId,
        LocalDate childbirthDate,
        ChildbirthGender gender,
        BigDecimal weight,
        ChildbirthCondition condition,
        UUID breedId,
        UUID calfId,
        String breedName,
        String calfIdentifier
) {

    public static ListAnimalChildbirthOutput from(final AnimalChildbirth entity, final String breedName, final String calfIdentifier) {
        return new ListAnimalChildbirthOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getChildbirthDate(),
                entity.getGender(),
                entity.getWeight(),
                entity.getCondition(),
                entity.getBreedId().id(),
                entity.getCalfId().map(id -> id.id()).orElse(null),
                breedName,
                calfIdentifier
        );
    }
}
