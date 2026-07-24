package br.gov.pr.idr.application.property_management.sync.download;

import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record DownloadSyncOutput(
        String schemaVersion,
        Instant serverTimestamp,
        Map<String, List<SyncItem>> collections
) {

    public List<SyncItem> collection(final String name) {
        return collections.getOrDefault(name, List.of());
    }
}
