package br.gov.pr.idr.application.livestock.animal_sales.create;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAnimalSalesCommand(
        UUID propertyId,
        UUID animalId,
        PropertySearchScope scope,
        LocalDate dateSale,
        Reason reason,
        BigDecimal value,
        Destination destination,
        BigDecimal weight
) {
}
