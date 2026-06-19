package br.gov.pr.idr.infra.property_management.property.models.search;

import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyOutput;

import java.math.BigDecimal;
import java.util.UUID;

public record SearchPropertyResponse(
        UUID id,
        String name,
        BigDecimal nakedAveragePrice,
        BigDecimal leaseAveragePrice,
        UUID producerId,
        UUID cityId
) {

    public static SearchPropertyResponse from(final SearchPropertyOutput output) {
        return new SearchPropertyResponse(
                output.id(),
                output.name(),
                output.nakedAveragePrice(),
                output.leaseAveragePrice(),
                output.producerId(),
                output.cityId()
        );
    }
}
