package br.gov.pr.idr.application.property_management.property.retrieve.search;

import br.gov.pr.idr.domain.property_management.property.Property;

import java.math.BigDecimal;
import java.util.UUID;

public record SearchPropertyOutput(
        UUID id,
        String name,
        BigDecimal nakedAveragePrice,
        BigDecimal leaseAveragePrice,
        UUID producerId,
        UUID cityId
) {

    public static SearchPropertyOutput from(final Property property) {
        return new SearchPropertyOutput(
                property.getId().id(),
                property.getName(),
                property.getNakedAveragePrice(),
                property.getLeaseAveragePrice(),
                property.getProducerId().id(),
                property.getCityId().id()
        );
    }
}
