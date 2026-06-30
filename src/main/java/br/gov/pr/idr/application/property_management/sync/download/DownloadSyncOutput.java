package br.gov.pr.idr.application.property_management.sync.download;

import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.sync.SyncSnapshot;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DownloadSyncOutput(
        String schemaVersion,
        List<RegionOutput> regions,
        List<CityOutput> cities,
        List<ProducerOutput> producers
) {
    public record RegionOutput(UUID id, String name) {}
    public record CityOutput(UUID id, String name, State state) {}
    public record ProducerOutput(UUID id, String name, String cpf, Long version, Instant updatedAt) {}

    public static DownloadSyncOutput from(final SyncSnapshot snapshot) {
        return new DownloadSyncOutput(
                "1.0",
                snapshot.regions().stream()
                        .map(r -> new RegionOutput(r.id(), r.name()))
                        .toList(),
                snapshot.cities().stream()
                        .map(c -> new CityOutput(c.id(), c.name(), c.state()))
                        .toList(),
                snapshot.producers().stream()
                        .map(p -> new ProducerOutput(p.id(), p.name(), p.cpf(), p.version(), p.updatedAt()))
                        .toList()
        );
    }
}
