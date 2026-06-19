package br.gov.pr.idr.application.property_management.property.update;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record UpdatePropertyCommand(
        UUID id,
        String name,
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

    public record CollaboratorData(String name, String hoursPerDay) {}

    public static UpdatePropertyCommand from(
            UUID id,
            String name,
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
        return new UpdatePropertyCommand(id, name, latitude, longitude, nakedAveragePrice, leaseAveragePrice,
                dairyCattleFarming, perennialPasture, summerPlowing, winterPlowing, producerId, cityId,
                technicianIds == null ? List.of() : technicianIds,
                collaborators == null ? List.of() : collaborators);
    }
}
