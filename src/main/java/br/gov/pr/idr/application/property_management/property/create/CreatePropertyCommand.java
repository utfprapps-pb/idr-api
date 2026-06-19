package br.gov.pr.idr.application.property_management.property.create;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreatePropertyCommand(String name,
                                    BigDecimal latitude,
                                    BigDecimal longitude,
                                    BigDecimal nakedAveragePrice,
                                    BigDecimal leaseAveragePrice,
                                    Double dairyCattleFarming,
                                    Double perennialPasture,
                                    Double summerPlowing,
                                    Double winterPlowing,
                                    UUID producerId,
                                    UUID cityId,
                                    List<UUID> technicianIds,
                                    List<CollaboratorData> collaborators) {

    public record CollaboratorData(String name, String hoursPerDay) {}

    public static CreatePropertyCommand from(String name,
                                             BigDecimal latitude,
                                             BigDecimal longitude,
                                             BigDecimal nakedAveragePrice,
                                             BigDecimal leaseAveragePrice,
                                             Double dairyCattleFarming,
                                             Double perennialPasture,
                                             Double summerPlowing,
                                             Double winterPlowing,
                                             UUID producerId,
                                             UUID cityId,
                                             List<UUID> technicianIds,
                                             List<CollaboratorData> collaborators
    ) {
        return new CreatePropertyCommand(name, latitude, longitude, nakedAveragePrice,
                leaseAveragePrice, dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing, producerId,
                cityId, technicianIds == null ? List.of() : technicianIds,
                collaborators == null ? List.of() : collaborators);
    }

}
