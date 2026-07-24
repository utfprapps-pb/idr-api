package br.gov.pr.idr.domain.property_management.sync.entity;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record SyncItem(
        UUID id,
        Map<String, Object> data,
        Long version,
        Instant updatedAt,
        boolean deleted
) {

    public static SyncItem active(final UUID id, final Map<String, Object> data,
                                  final Long version, final Instant updatedAt) {
        return new SyncItem(id, data, version, updatedAt, false);
    }

    public static SyncItem tombstone(final UUID id, final Long version, final Instant updatedAt) {
        return new SyncItem(id, Map.of(), version, updatedAt, true);
    }
}
