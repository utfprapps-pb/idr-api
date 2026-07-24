package br.gov.pr.idr.infra.property_management.city.sync;

import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPARepository;
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
@DisplayName("CitySyncContributor")
class CitySyncContributorTest {

    @Mock CityJPARepository repository;
    @InjectMocks CitySyncContributor contributor;

    private CityJPAEntity city(final String name) {
        return CityJPAEntity.from(City.with(CityID.unique(), name, State.PR, RegionID.unique()));
    }

    private CityJPAEntity deletedCity(final String name) {
        final var entity = city(name);
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
    @DisplayName("contribute() snapshot por cityIds deve retornar itens ativos")
    void shouldReturnActiveItemsBySnapshotCityIds() {
        final var cityId = CityID.unique();
        when(repository.findAllById(any())).thenReturn(List.of(city("Curitiba")));
        final var scope = new SyncScope(UserID.unique(), Set.of(), Set.of(cityId), null);

        final var items = contributor.contribute(scope);

        assertEquals(1, items.size());
        assertEquals("Curitiba", items.get(0).data().get("name"));
        assertFalse(items.get(0).deleted());
    }

    @Test
    @DisplayName("contribute() incremental por regionIds deve retornar ativos e tombstones")
    void shouldReturnActiveAndTombstonesByIncrementalRegionIds() {
        final var regionId = RegionID.unique();
        final var since = Instant.parse("2026-07-01T00:00:00Z");
        when(repository.findAllByRegionIdInAndUpdatedAtAfter(any(), any())).thenReturn(List.of(city("Ativa")));
        when(repository.findDeletedSince(any(), any())).thenReturn(List.of(deletedCity("Excluída")));
        final var scope = new SyncScope(UserID.unique(), Set.of(regionId), Set.of(), since);

        final var items = contributor.contribute(scope);

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> !i.deleted()));
        assertTrue(items.stream().anyMatch(i -> i.deleted()));
    }

    @Test
    @DisplayName("contribute() incremental por cityIds deve retornar ativos e tombstones")
    void shouldReturnActiveAndTombstonesByIncrementalCityIds() {
        final var cityId = CityID.unique();
        final var since = Instant.parse("2026-07-01T00:00:00Z");
        when(repository.findAllByIdInAndUpdatedAtAfter(any(), any())).thenReturn(List.of(city("Ativa")));
        when(repository.findDeletedByIdsSince(any(), any())).thenReturn(List.of(deletedCity("Excluída")));
        final var scope = new SyncScope(UserID.unique(), Set.of(), Set.of(cityId), since);

        final var items = contributor.contribute(scope);

        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(i -> !i.deleted()));
        assertTrue(items.stream().anyMatch(i -> i.deleted()));
    }

    @Test
    @DisplayName("collectionName() deve retornar 'cities'")
    void shouldExposeCollectionName() {
        assertEquals("cities", contributor.collectionName());
    }
}
