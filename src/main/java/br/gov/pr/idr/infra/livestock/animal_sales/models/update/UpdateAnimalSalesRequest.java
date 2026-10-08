package br.gov.pr.idr.infra.livestock.animal_sales.models.update;

import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateAnimalSalesRequest(
        LocalDate dateSale,
        Reason reason,
        BigDecimal value,
        Destination destination
) {
}
