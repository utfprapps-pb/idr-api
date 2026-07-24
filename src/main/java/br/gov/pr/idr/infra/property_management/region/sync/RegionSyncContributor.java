package br.gov.pr.idr.infra.property_management.region.sync;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.snapshot.SyncSnapshotContributor;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegionSyncContributor implements SyncSnapshotContributor {

    private final RegionJPARepository repository;

    @Override
    public String collectionName() {
        return "regions";
    }

    @Override
    public List<SyncItem> contribute(final SyncScope scope) {
        final Set<UUID> regionIds = scope.regionUuids();
        if (regionIds.isEmpty()) {
            return List.of();
        }

        if (!scope.isIncremental()) {
            return repository.findAllByIdIn(regionIds).stream().map(RegionSyncContributor::toActiveItem).toList();
        }

        final var since = scope.since();
        final List<SyncItem> items = new ArrayList<>();
        repository.findAllByIdInAndUpdatedAtAfter(regionIds, since).forEach(r -> items.add(toActiveItem(r)));
        repository.findDeletedSince(regionIds, since).forEach(r -> items.add(toTombstone(r)));
        return items;
    }

    private static SyncItem toActiveItem(final RegionJPAEntity region) {
        return SyncItem.active(region.getId(), Map.of("name", region.getDescription()),
                region.getVersion(), region.getUpdatedAt());
    }

    private static SyncItem toTombstone(final RegionJPAEntity region) {
        return SyncItem.tombstone(region.getId(), region.getVersion(), region.getDeletedAt());
    }
}
