package br.gov.pr.idr.infra.property_management.producer.sync;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.producer.Producer;
import br.gov.pr.idr.domain.property_management.producer.ProducerID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPAEntity;
import br.gov.pr.idr.infra.property_management.producer.persistence.ProducerJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProducerSyncContributor")
class ProducerSyncContributorTest {

    @Mock ProducerJPARepository producerRepository;
    @Mock CityJPARepository cityRepository;
    @InjectMocks ProducerSyncContributor contributor;

    private ProducerJPAEntity producer(final String name) {
        return ProducerJPAEntity.fromDomain(Producer.with(ProducerID.unique(), name, CPF.from("529.982.247-25")));
    }

    private ProducerJPAEntity deletedProducer(final String name) {
        final var entity = producer(name);
        entity.markDeleted();
        return entity;
    }

    @Test
    @DisplayName("contribute() deve retornar lista vazia quando o escopo não possui região nem cidade")
    void shouldReturnEmptyWhenScopeHasNoLocation() {
        final var scope = new SyncScope(UserID.unique(), Set.of(), Set.of(), null);

        assertTrue(contributor.contribute(scope).isEmpty());
    }

    @Test
    @DisplayName("contribute() deve retornar lista vazia quando as cidades resolvidas estão vazias")
    void shouldReturnEmptyWhenNoCitiesResolved() {
        final var regionId = RegionID.unique();
        when(cityRepository.findAllByRegionIdIn(any())).thenReturn(List.of());
        final var scope = new SyncScope(UserID.unique(), Set.of(regionId), Set.of(), null);

        assertTrue(contributor.contribute(scope).isEmpty());
    }

    @Test
    @DisplayName("contribute() snapshot deve retornar produtores ativos das cidades resolvidas por região")
    void shouldReturnActiveProducersBySnapshot() {
        final var regionId = RegionID.unique();
        final var city = CityJPAEntity.from(City.with(CityID.unique(), "Curitiba", State.PR, regionId));
        when(cityRepository.findAllByRegionIdIn(any())).thenReturn(List.of(city));
        when(producerRepository.findAllByCityIds(any())).thenReturn(List.of(producer("João")));
        final var scope = new SyncScope(UserID.unique(), Set.of(regionId), Set.of(), null);

        final var items = contributor.contribute(scope);

        assertEquals(1, items.size());
        assertEquals("João", items.get(0).data().get("name"));
        assertFalse(items.get(0).deleted());
    }

    @Test
    @DisplayName("contribute() incremental deve retornar produtores ativos e tombstones das cidades informadas")
    void shouldReturnActiveAndTombstonesIncremental() {
        final var cityId = CityID.unique();
        final var since = Instant.parse("2026-07-01T00:00:00Z");
        when(producerRepository.findAllByCityIdsAndUpdatedAtAfter(any(), any())).thenReturn(List.of(producer("Ativo")));
        when(producerRepository.findDeletedSince(any(), any())).thenReturn(List.of(deletedProducer("Excluído")));
        final var scope = new SyncScope(UserID.unique(), Set.of(), Set.of(cityId), since);

        final var items = contributor.contribute(scope);

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> !i.deleted()));
        assertTrue(items.stream().anyMatch(i -> i.deleted()));
    }

    @Test
    @DisplayName("collectionName() deve retornar 'producers'")
    void shouldExposeCollectionName() {
        assertEquals("producers", contributor.collectionName());
    }
}
