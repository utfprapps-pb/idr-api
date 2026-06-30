package br.gov.pr.idr.application.property_management.sync.upload;

import br.gov.pr.idr.domain.property_management.sync.OfflineEntityType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UploadSyncCommand(List<OfflineEntityCommand> entities) {

    public record OfflineEntityCommand(
            OfflineEntityType type,
            UUID localId,
            Map<String, Object> data
    ) {}
}
