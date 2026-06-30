package br.gov.pr.idr.infra.property_management.property.models.sync;

import br.gov.pr.idr.domain.property_management.sync.OfflineEntityType;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record OfflineEntityRequest(
        @NotNull OfflineEntityType type,
        @NotNull UUID localId,
        @NotNull Map<String, Object> data
) {}
