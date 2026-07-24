package br.gov.pr.idr.infra.property_management.property.models.sync;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UploadSyncRequest(@NotNull @Valid List<OfflineEntityRequest> entities) {}
