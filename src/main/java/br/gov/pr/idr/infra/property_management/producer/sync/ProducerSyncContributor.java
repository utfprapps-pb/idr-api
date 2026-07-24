package br.gov.pr.idr.infra.property_management.producer.sync;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.snapshot.SyncSnapshotContributor;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProducerSyncContributor implements SyncSnapshotContributor {

    private final ProducerJPARepository producerRepository;
    private final CityJPARepository cityRepository;

    @Override
    public String collectionName() {
        return "producers";
    }

    @Override
    public List<SyncItem> contribute(final SyncScope scope) {
        if (scope.hasNoScope()) {
            return List.of();
        }

        final Set<UUID> regionIds = scope.regionUuids();
        final Set<UUID> cityIds = new HashSet<>(scope.cityUuids());
        if (!regionIds.isEmpty()) {
            cityRepository.findAllByRegionIdIn(regionIds).stream()
                    .map(CityJPAEntity::getId)
                    .forEach(cityIds::add);
        }

        if (cityIds.isEmpty()) {
            return List.of();
        }

        if (!scope.isIncremental()) {
            return producerRepository.findAllByCityIds(cityIds).stream()
                    .map(ProducerSyncContributor::toActiveItem)
                    .toList();
        }

        final var since = scope.since();
        final List<SyncItem> items = new ArrayList<>();
        producerRepository.findAllByCityIdsAndUpdatedAtAfter(cityIds, since).forEach(p -> items.add(toActiveItem(p)));
        producerRepository.findDeletedSince(cityIds, since).forEach(p -> items.add(toTombstone(p)));
        return items;
    }

    private static SyncItem toActiveItem(final ProducerJPAEntity producer) {
        return SyncItem.active(producer.getId(), Map.of("name", producer.getName(), "cpf", producer.getCpf()),
                producer.getVersion(), producer.getUpdatedAt());
    }

    private static SyncItem toTombstone(final ProducerJPAEntity producer) {
        return SyncItem.tombstone(producer.getId(), producer.getVersion(), producer.getDeletedAt());
    }
}
