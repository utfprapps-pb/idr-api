package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncUseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.sync.SyncDownloadGateway;
import br.gov.pr.idr.domain.property_management.sync.SyncSnapshot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DownloadSyncUseCase")
class DownloadSyncUseCaseTest {

    @Mock SyncDownloadGateway syncDownloadGateway;
    @InjectMocks DownloadSyncUseCase useCase;

    @Test
    @DisplayName("deve retornar snapshot com cidades, regiões e produtores quando técnico tem regiões")
    void shouldReturnSnapshotWhenTechnicianHasRegions() {
        final var regionId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var producerId = UUID.randomUUID();

        final var snapshot = new SyncSnapshot(
                List.of(new SyncSnapshot.RegionInfo(regionId, "Norte")),
                List.of(new SyncSnapshot.CityInfo(cityId, "Pato Branco", State.PR)),
                List.of(new SyncSnapshot.ProducerInfo(producerId, "João Silva", "529.982.247-25", 0L, null))
        );
        when(syncDownloadGateway.findByTechnicianId(any())).thenReturn(snapshot);

        final var output = useCase.execute(UserID.from(UUID.randomUUID()));

        assertNotNull(output);
        assertEquals("1.0", output.schemaVersion());
        assertEquals(1, output.regions().size());
        assertEquals(1, output.cities().size());
        assertEquals(1, output.producers().size());
        assertEquals("João Silva", output.producers().getFirst().name());
    }

    @Test
    @DisplayName("deve retornar listas vazias quando técnico não tem regiões")
    void shouldReturnEmptyListsWhenTechnicianHasNoRegions() {
        final var snapshot = new SyncSnapshot(List.of(), List.of(), List.of());
        when(syncDownloadGateway.findByTechnicianId(any())).thenReturn(snapshot);

        final var output = useCase.execute(UserID.from(UUID.randomUUID()));

        assertEquals("1.0", output.schemaVersion());
        assertTrue(output.regions().isEmpty());
        assertTrue(output.cities().isEmpty());
        assertTrue(output.producers().isEmpty());
    }
}
