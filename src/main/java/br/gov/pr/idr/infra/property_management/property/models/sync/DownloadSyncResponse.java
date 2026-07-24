package br.gov.pr.idr.infra.property_management.property.models.sync;

import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncOutput;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DownloadSyncResponse(
        String schemaVersion,
        Instant serverTimestamp,
        List<RegionResponse> regions,
        List<CityResponse> cities,
        List<ProducerResponse> producers,
        List<TechnicianResponse> technicians
) {
    public record RegionResponse(UUID id, String name, boolean deleted) {}
    public record CityResponse(UUID id, String name, State state, boolean deleted) {}
    public record ProducerResponse(UUID id, String name, String cpf, Long version, Instant updatedAt, boolean deleted) {}
    public record TechnicianResponse(UUID id, String name, boolean deleted) {}

    public static DownloadSyncResponse from(final DownloadSyncOutput output) {
        return new DownloadSyncResponse(
                output.schemaVersion(),
                output.serverTimestamp(),
                output.collection("regions").stream()
                        .map(i -> new RegionResponse(i.id(), string(i, "name"), i.deleted()))
                        .toList(),
                output.collection("cities").stream()
                        .map(i -> new CityResponse(i.id(), string(i, "name"), state(i), i.deleted()))
                        .toList(),
                output.collection("producers").stream()
                        .map(i -> new ProducerResponse(i.id(), string(i, "name"), string(i, "cpf"),
                                i.version(), i.updatedAt(), i.deleted()))
                        .toList(),
                output.collection("technicians").stream()
                        .map(i -> new TechnicianResponse(i.id(), string(i, "name"), i.deleted()))
                        .toList()
        );
    }

    private static String string(final SyncItem item, final String key) {
        final var value = item.data().get(key);
        return value == null ? null : value.toString();
    }

    private static State state(final SyncItem item) {
        final var value = item.data().get("state");
        return value instanceof State s ? s : null;
    }
}
