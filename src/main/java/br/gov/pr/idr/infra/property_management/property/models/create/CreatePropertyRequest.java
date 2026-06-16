package br.gov.pr.idr.infra.property_management.property.models.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CreatePropertyRequest(@NotBlank(message = "Nome não pode ser nulo") String name,
                                    @NotNull(message = "Latitude não pode ser nulo") BigDecimal latitude,
                                    @NotNull(message = "Longitue não pode ser nulo") BigDecimal longitude,
                                    BigDecimal totalArea,
                                    Boolean leased,
                                    BigDecimal nakedAveragePrice,
                                    BigDecimal leaseAveragePrice,
                                    Double dairyCattleFarming,
                                    Double perennialPasture,
                                    Double summerPlowing,
                                    Double winterPlowing,
                                    @NotNull(message = "Produtor não pode ser nulo") UUID producerId,
                                    @NotNull(message = "Cidade não pode ser nulo") UUID cityId,
                                    List<UUID> technicianIds,
                                    List<CollaboratorRequest> collaborators) {

    public record CollaboratorRequest(@NotBlank(message = "Nome do colaborador não pode ser nulo") String name,
                                      @NotBlank(message = "Horas por dia não pode ser nulo") String hoursPerDay) {
    }
}
