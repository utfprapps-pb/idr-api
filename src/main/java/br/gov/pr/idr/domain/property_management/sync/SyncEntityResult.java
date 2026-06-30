package br.gov.pr.idr.domain.property_management.sync;

import java.util.UUID;

public record SyncEntityResult(
        UUID localId,
        UUID serverId,
        SyncEntityStatus status,
        String message
) {}
