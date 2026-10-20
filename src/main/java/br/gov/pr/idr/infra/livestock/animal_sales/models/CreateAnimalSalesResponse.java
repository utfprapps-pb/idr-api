package br.gov.pr.idr.infra.livestock.animal_sales.models;

import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesOutput;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalSalesResponse(
        UUID id,
        UUID animalId,
        LocalDate dateSale,
        Reason reason,
        BigDecimal value,
        Destination destination,
        BigDecimal weight
) {

    public static CreateAnimalSalesResponse from(final CreateAnimalSalesOutput output) {
        return new CreateAnimalSalesResponse(
                output.id(),
                output.animalId(),
                output.dateSale(),
                output.reason(),
                output.value(),
                output.destination(),
                output.weight()
        );
    }
}
