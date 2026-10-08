package br.gov.pr.idr.application.livestock.animal_sales.create;

import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalSalesOutput(
        UUID id,
        UUID animalId,
        LocalDate dateSale,
        Reason reason,
        BigDecimal value,
        Destination destination
) {

    public static CreateAnimalSalesOutput from(final AnimalSales entity) {
        return new CreateAnimalSalesOutput(
                entity.getId().id(),
                entity.getAnimalId().id(),
                entity.getDateSale(),
                entity.getReason(),
                entity.getValue(),
                entity.getDestination()
        );
    }
}
