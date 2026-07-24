package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.domain.property_management.sync.query.DownloadSyncQuery;
import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncUseCase;
import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.property_management.sync.snapshot.SyncSnapshotContributor;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncItem;
import br.gov.pr.idr.domain.property_management.sync.scope.SyncScopeGateway;
import br.gov.pr.idr.domain.property_management.sync.technician.TechnicianScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DownloadSyncUseCase")
class DownloadSyncUseCaseTest {

    @Mock SyncScopeGateway scopeGateway;
    @Mock SyncSnapshotContributor regionsContributor;
    @Mock SyncSnapshotContributor producersContributor;

    @Test
    @DisplayName("deve agregar contribuidores por nome de coleção e capturar serverTimestamp")
    void shouldAggregateContributorsKeyedByCollectionName() {
        final var regionItem = SyncItem.active(UUID.randomUUID(), Map.of("name", "Norte"), 0L, Instant.now());
        final var producerItem = SyncItem.active(UUID.randomUUID(), Map.of("name", "João"), 0L, Instant.now());

        when(scopeGateway.resolveByTechnician(any()))
                .thenReturn(new TechnicianScope(Set.of(RegionID.unique()), Set.of()));
        when(regionsContributor.collectionName()).thenReturn("regions");
        when(regionsContributor.contribute(any())).thenReturn(List.of(regionItem));
        when(producersContributor.collectionName()).thenReturn("producers");
        when(producersContributor.contribute(any())).thenReturn(List.of(producerItem));

        final var useCase = new DownloadSyncUseCase(
                List.of(regionsContributor, producersContributor), scopeGateway);

        final var output = useCase.execute(new DownloadSyncQuery(UserID.unique(), null));

        assertEquals("1.0", output.schemaVersion());
        assertNotNull(output.serverTimestamp());
        assertEquals(List.of(regionItem), output.collection("regions"));
        assertEquals(List.of(producerItem), output.collection("producers"));
    }

    @Test
    @DisplayName("deve resolver o escopo uma única vez e propagar since aos contribuidores")
    void shouldResolveScopeOnceAndPropagateSince() {
        final var since = Instant.parse("2026-07-01T00:00:00Z");
        final var regionId = RegionID.unique();

        when(scopeGateway.resolveByTechnician(any()))
                .thenReturn(new TechnicianScope(Set.of(regionId), Set.of()));
        when(regionsContributor.collectionName()).thenReturn("regions");
        when(regionsContributor.contribute(any())).thenReturn(List.of());

        final var useCase = new DownloadSyncUseCase(List.of(regionsContributor), scopeGateway);
        useCase.execute(new DownloadSyncQuery(UserID.unique(), since));

        verify(scopeGateway, times(1)).resolveByTechnician(any());
        final var captor = ArgumentCaptor.forClass(SyncScope.class);
        verify(regionsContributor).contribute(captor.capture());
        final var scope = captor.getValue();
        assertTrue(scope.isIncremental());
        assertEquals(since, scope.since());
        assertEquals(Set.of(regionId), scope.regionIds());
    }
}
