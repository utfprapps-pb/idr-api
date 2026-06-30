package br.gov.pr.idr.domain.property_management.sync;

import br.gov.pr.idr.domain.property_management.city.vo.State;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SyncSnapshot(
        List<RegionInfo> regions,
        List<CityInfo> cities,
        List<ProducerInfo> producers
) {
    public record RegionInfo(UUID id, String name) {}
    public record CityInfo(UUID id, String name, State state) {}
    public record ProducerInfo(UUID id, String name, String cpf, Long version, Instant updatedAt) {}
}
