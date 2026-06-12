package br.gov.pr.idr.application.property_management.property.create;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePropertyCommand(String name,
                                    BigDecimal latitude,
                                    BigDecimal longitude,
                                    BigDecimal totalArea,
                                    Boolean leased,
                                    BigDecimal nakedAveragePrice,
                                    BigDecimal leaseAveragePrice,
                                    Double dairyCattleFarming,
                                    Double perennialPasture,
                                    Double summerPlowing,
                                    Double winterPlowing,
                                    UUID producerId,
                                    UUID cityId) {

    public static CreatePropertyCommand from(String name,
                                             BigDecimal latitude,
                                             BigDecimal longitude,
                                             BigDecimal totalArea,
                                             Boolean leased,
                                             BigDecimal nakedAveragePrice,
                                             BigDecimal leaseAveragePrice,
                                             Double dairyCattleFarming,
                                             Double perennialPasture,
                                             Double summerPlowing,
                                             Double winterPlowing,
                                             UUID producerId,
                                             UUID cityId
    ) {
        return new CreatePropertyCommand(name, latitude, longitude, totalArea, leased, nakedAveragePrice,
                leaseAveragePrice, dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing, producerId,
                cityId);
    }

}
