package br.gov.pr.idr.application.property_management.sync;

import br.gov.pr.idr.domain.property_management.sync.query.DownloadSyncQuery;
import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncUseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.region.Region;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.property_management.sync.scope.SyncScopeGateway;
import br.gov.pr.idr.domain.property_management.sync.technician.TechnicianScope;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPARepository;
import br.gov.pr.idr.infra.property_management.city.sync.CitySyncContributor;
import br.gov.pr.idr.infra.property_management.producer.sync.ProducerSyncContributor;
import br.gov.pr.idr.infra.property_management.region.sync.RegionSyncContributor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Teste de integração do pipeline de download: use case real, contribuidores reais e
 * mapeamento de resposta, com repositórios mockados (o projeto não possui harness de
 * banco). Cobre os cenários end-to-end do download incremental.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Download sync (integração do pipeline)")
class DownloadSyncIntegrationTest {

    @Mock RegionJPARepository regionRepository;
    @Mock CityJPARepository cityRepository;
    @Mock ProducerJPARepository producerRepository;

    private final RegionID regionId = RegionID.unique();

    private DownloadSyncUseCase useCaseFor(final Set<RegionID> regionIds) {
        final SyncScopeGateway scopeGateway = technicianId -> new TechnicianScope(regionIds, Set.of());
        return new DownloadSyncUseCase(
                List.of(
                        new RegionSyncContributor(regionRepository),
                        new CitySyncContributor(cityRepository),
                        new ProducerSyncContributor(producerRepository, cityRepository)
                ),
                scopeGateway);
    }

    @Test
    @DisplayName("snapshot completo (sem since) retorna coleções ativas sem tombstones")
    void shouldReturnFullSnapshotWithoutSince() {
        final var city = city();
        when(regionRepository.findAllByIdIn(any())).thenReturn(List.of(region("Norte")));
        when(cityRepository.findAllByRegionIdIn(any())).thenReturn(List.of(city));
        when(producerRepository.findAllByCityIds(any())).thenReturn(List.of(producer()));

        final var output = useCaseFor(Set.of(regionId)).execute(new DownloadSyncQuery(UserID.unique(), null));

        assertEquals(1, output.collection("regions").size());
        assertEquals(1, output.collection("cities").size());
        assertEquals(1, output.collection("producers").size());
        assertTrue(output.collection("regions").stream().noneMatch(i -> i.deleted()));
        assertNotNull(output.serverTimestamp());
    }

    @Test
    @DisplayName("delta com since retorna apenas registros alterados após o instante")
    void shouldReturnOnlyDeltaWithSince() {
        final var since = Instant.parse("2026-07-01T00:00:00Z");
        when(regionRepository.findAllByIdInAndUpdatedAtAfter(any(), any())).thenReturn(List.of(region("Alterada")));
        when(cityRepository.findAllByRegionIdIn(any())).thenReturn(List.of(city()));

        final var output = useCaseFor(Set.of(regionId)).execute(new DownloadSyncQuery(UserID.unique(), since));

        assertEquals(1, output.collection("regions").size());
        assertFalse(output.collection("regions").getFirst().deleted());
    }

    @Test
    @DisplayName("delta com since inclui tombstones de registros soft-deletados")
    void shouldIncludeTombstonesInDelta() {
        final var since = Instant.parse("2026-07-01T00:00:00Z");
        when(regionRepository.findDeletedSince(any(), any())).thenReturn(List.of(deletedRegion()));

        final var output = useCaseFor(Set.of(regionId)).execute(new DownloadSyncQuery(UserID.unique(), since));

        final var regions = output.collection("regions");
        assertEquals(1, regions.size());
        assertTrue(regions.getFirst().deleted());
    }

    @Test
    @DisplayName("técnico sem regiões retorna todas as coleções vazias")
    void shouldReturnEmptyWhenTechnicianHasNoRegions() {
        final var output = useCaseFor(Set.of()).execute(new DownloadSyncQuery(UserID.unique(), null));

        assertTrue(output.collection("regions").isEmpty());
        assertTrue(output.collection("cities").isEmpty());
        assertTrue(output.collection("producers").isEmpty());
    }

    @Test
    @DisplayName("resposta expõe serverTimestamp para encadeamento das sincronizações")
    void shouldExposeServerTimestampForChaining() {
        when(regionRepository.findAllByIdIn(any())).thenReturn(List.of());
        when(cityRepository.findAllByRegionIdIn(any())).thenReturn(List.of());

        final var first = useCaseFor(Set.of(regionId)).execute(new DownloadSyncQuery(UserID.unique(), null));
        assertNotNull(first.serverTimestamp());

        // o cliente reutiliza o serverTimestamp anterior como since da próxima sincronização
        final var second = useCaseFor(Set.of(regionId))
                .execute(new DownloadSyncQuery(UserID.unique(), first.serverTimestamp()));
        assertNotNull(second.serverTimestamp());
    }

    // --- fixtures ---

    private RegionJPAEntity region(final String name) {
        return RegionJPAEntity.from(Region.with(RegionID.unique(), name));
    }

    private RegionJPAEntity deletedRegion() {
        final var region = region("Excluída");
        region.markDeleted();
        return region;
    }

    private CityJPAEntity city() {
        return CityJPAEntity.from(City.with(CityID.unique(), "Pato Branco", State.PR, regionId));
    }

    private ProducerJPAEntity producer() {
        return ProducerJPAEntity.fromDomain(Producer.with(ProducerID.unique(), "João", CPF.from("529.982.247-25")));
    }
}
