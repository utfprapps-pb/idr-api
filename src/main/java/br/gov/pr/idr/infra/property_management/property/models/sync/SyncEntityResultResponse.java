package br.gov.pr.idr.infra.property_management.property.models.sync;

import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;

import java.util.UUID;

public record SyncEntityResultResponse(
        UUID localId,
        UUID serverId,
        SyncEntityStatus status,
        String message
) {
    public static SyncEntityResultResponse from(final SyncEntityResult result) {
        return new SyncEntityResultResponse(
                result.localId(),
                result.serverId(),
                result.status(),
                result.message()
        );
    }
}
