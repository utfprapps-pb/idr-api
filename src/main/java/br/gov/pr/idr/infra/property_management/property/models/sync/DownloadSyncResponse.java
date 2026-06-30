package br.gov.pr.idr.infra.property_management.property.models.sync;

import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncOutput;
import br.gov.pr.idr.domain.property_management.city.vo.State;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DownloadSyncResponse(
        String schemaVersion,
        List<RegionResponse> regions,
        List<CityResponse> cities,
        List<ProducerResponse> producers
) {
    public record RegionResponse(UUID id, String name) {}
    public record CityResponse(UUID id, String name, State state) {}
    public record ProducerResponse(UUID id, String name, String cpf, Long version, Instant updatedAt) {}

    public static DownloadSyncResponse from(final DownloadSyncOutput output) {
        return new DownloadSyncResponse(
                output.schemaVersion(),
                output.regions().stream()
                        .map(r -> new RegionResponse(r.id(), r.name()))
                        .toList(),
                output.cities().stream()
                        .map(c -> new CityResponse(c.id(), c.name(), c.state()))
                        .toList(),
                output.producers().stream()
                        .map(p -> new ProducerResponse(p.id(), p.name(), p.cpf(), p.version(), p.updatedAt()))
                        .toList()
        );
    }
}
