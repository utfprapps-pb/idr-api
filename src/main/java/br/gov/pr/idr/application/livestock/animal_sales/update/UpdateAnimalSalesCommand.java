package br.gov.pr.idr.application.livestock.animal_sales.update;

import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateAnimalSalesCommand(
        UUID propertyId,
        UUID animalId,
        UUID animalSalesId,
        String username,
        LocalDate dateSale,
        Reason reason,
        BigDecimal value,
        Destination destination,
        BigDecimal weight
) {
}
