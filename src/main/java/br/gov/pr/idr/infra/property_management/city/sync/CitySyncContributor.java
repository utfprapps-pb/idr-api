package br.gov.pr.idr.infra.property_management.city.sync;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.snapshot.SyncSnapshotContributor;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CitySyncContributor implements SyncSnapshotContributor {

    private final CityJPARepository repository;

    @Override
    public String collectionName() {
        return "cities";
    }

    @Override
    public List<SyncItem> contribute(final SyncScope scope) {
        if (scope.hasNoScope()) {
            return List.of();
        }

        final Set<UUID> regionIds = scope.regionUuids();
        final Set<UUID> cityIds = scope.cityUuids();

        if (!scope.isIncremental()) {
            final Map<UUID, CityJPAEntity> active = new LinkedHashMap<>();
            if (!regionIds.isEmpty()) {
                repository.findAllByRegionIdIn(regionIds).forEach(c -> active.put(c.getId(), c));
            }
            if (!cityIds.isEmpty()) {
                repository.findAllById(cityIds).forEach(c -> active.put(c.getId(), c));
            }
            return active.values().stream().map(CitySyncContributor::toActiveItem).toList();
        }

        final var since = scope.since();
        final Map<UUID, SyncItem> items = new LinkedHashMap<>();
        if (!regionIds.isEmpty()) {
            repository.findAllByRegionIdInAndUpdatedAtAfter(regionIds, since)
                    .forEach(c -> items.put(c.getId(), toActiveItem(c)));
            repository.findDeletedSince(regionIds, since)
                    .forEach(c -> items.put(c.getId(), toTombstone(c)));
        }
        if (!cityIds.isEmpty()) {
            repository.findAllByIdInAndUpdatedAtAfter(cityIds, since)
                    .forEach(c -> items.put(c.getId(), toActiveItem(c)));
            repository.findDeletedByIdsSince(cityIds, since)
                    .forEach(c -> items.put(c.getId(), toTombstone(c)));
        }
        return List.copyOf(items.values());
    }

    private static SyncItem toActiveItem(final CityJPAEntity city) {
        return SyncItem.active(city.getId(), Map.of("name", city.getName(), "state", city.getState()),
                city.getVersion(), city.getUpdatedAt());
    }

    private static SyncItem toTombstone(final CityJPAEntity city) {
        return SyncItem.tombstone(city.getId(), city.getVersion(), city.getDeletedAt());
    }
}
