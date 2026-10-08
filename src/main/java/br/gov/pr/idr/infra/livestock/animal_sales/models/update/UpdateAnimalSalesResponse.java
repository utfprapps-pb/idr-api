package br.gov.pr.idr.infra.livestock.animal_sales.models.update;

import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesOutput;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalSalesResponse(
        UUID id,
        UUID animalId,
        LocalDate dateSale,
        Reason reason,
        BigDecimal value,
        Destination destination
) {

    public static UpdateAnimalSalesResponse from(final UpdateAnimalSalesOutput output) {
        return new UpdateAnimalSalesResponse(
                output.id(),
                output.animalId(),
                output.dateSale(),
                output.reason(),
                output.value(),
                output.destination()
        );
    }
}
