package br.gov.pr.idr.infra.property_management.property.models.sync;

import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncOutput;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DownloadSyncResponse")
class DownloadSyncResponseTest {

    @Test
    @DisplayName("from() deve mapear regiões, cidades, produtores e técnicos do output para a resposta")
    void shouldMapAllCollectionsFromOutput() {
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var producerId = UUID.randomUUID();
        final var technicianId = UUID.randomUUID();
        final var updatedAt = Instant.now();

        final var output = new DownloadSyncOutput(
                "1.0",
                updatedAt,
                Map.of(
                        "regions", List.of(SyncItem.active(regionId, Map.of("name", "Região Sul"), 1L, updatedAt)),
                        "cities", List.of(SyncItem.active(cityId, Map.of("name", "Curitiba", "state", State.PR), 1L, updatedAt)),
                        "producers", List.of(SyncItem.active(producerId,
                                Map.of("name", "João", "cpf", "529.982.247-25"), 1L, updatedAt)),
                        "technicians", List.of(SyncItem.active(technicianId, Map.of("name", "Carlos"), 1L, updatedAt))
                )
        );

        final var response = DownloadSyncResponse.from(output);

        assertEquals("1.0", response.schemaVersion());
        assertEquals(updatedAt, response.serverTimestamp());

        assertEquals(1, response.regions().size());
        assertEquals(regionId, response.regions().get(0).id());
        assertEquals("Região Sul", response.regions().get(0).name());
        assertFalse(response.regions().get(0).deleted());

        assertEquals(1, response.cities().size());
        assertEquals(cityId, response.cities().get(0).id());
        assertEquals("Curitiba", response.cities().get(0).name());
        assertEquals(State.PR, response.cities().get(0).state());

        assertEquals(1, response.producers().size());
        assertEquals(producerId, response.producers().get(0).id());
        assertEquals("João", response.producers().get(0).name());
        assertEquals("529.982.247-25", response.producers().get(0).cpf());
        assertEquals(1L, response.producers().get(0).version());
        assertEquals(updatedAt, response.producers().get(0).updatedAt());

        assertEquals(1, response.technicians().size());
        assertEquals(technicianId, response.technicians().get(0).id());
        assertEquals("Carlos", response.technicians().get(0).name());
    }

    @Test
    @DisplayName("from() com coleções vazias deve retornar listas vazias")
    void shouldMapEmptyCollectionsFromOutput() {
        final var output = new DownloadSyncOutput("1.0", Instant.now(), Map.of());

        final var response = DownloadSyncResponse.from(output);

        assertTrue(response.regions().isEmpty());
        assertTrue(response.cities().isEmpty());
        assertTrue(response.producers().isEmpty());
        assertTrue(response.technicians().isEmpty());
    }

    @Test
    @DisplayName("from() deve mapear cidade tombstoned com campos nulos quando data está vazio")
    void shouldMapTombstonedCityWithNullFields() {
        final var cityId = UUID.randomUUID();
        final var output = new DownloadSyncOutput("1.0", Instant.now(), Map.of(
                "cities", List.of(SyncItem.tombstone(cityId, 2L, Instant.now()))
        ));

        final var response = DownloadSyncResponse.from(output);

        final var city = response.cities().get(0);
        assertEquals(cityId, city.id());
        assertNull(city.name());
        assertNull(city.state());
        assertTrue(city.deleted());
    }
}
