package br.gov.pr.idr.application.property_management.sync.download.snapshot;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.property_management.sync.scope.SyncScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SyncScope — Value Object")
class SyncScopeTest {

    @Nested
    @DisplayName("Null-safety")
    class NullSafety {

        @Test
        @DisplayName("deve rejeitar technicianId nulo")
        void shouldRejectNullTechnicianId() {
            assertThrows(NullPointerException.class,
                    () -> new SyncScope(null, Set.of(), Set.of(), null));
        }

        @Test
        @DisplayName("deve substituir regionIds e cityIds nulos por conjuntos vazios")
        void shouldReplaceBothNullSetsWithEmptySets() {
            final var scope = new SyncScope(UserID.from(UUID.randomUUID()), null, null, null);

            assertNotNull(scope.regionIds());
            assertTrue(scope.regionIds().isEmpty());
            assertNotNull(scope.cityIds());
            assertTrue(scope.cityIds().isEmpty());
        }

        @Test
        @DisplayName("deve manter regionIds e cityIds informados quando ambos não nulos")
        void shouldKeepBothSetsWhenNonNull() {
            final var regionId = RegionID.unique();
            final var cityId = CityID.unique();

            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(regionId), Set.of(cityId), null);

            assertEquals(Set.of(regionId), scope.regionIds());
            assertEquals(Set.of(cityId), scope.cityIds());
        }

        @Test
        @DisplayName("deve substituir apenas regionIds nulo, mantendo cityIds informado")
        void shouldReplaceOnlyNullRegionIds() {
            final var cityId = CityID.unique();

            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), null, Set.of(cityId), null);

            assertTrue(scope.regionIds().isEmpty());
            assertEquals(Set.of(cityId), scope.cityIds());
        }

        @Test
        @DisplayName("deve substituir apenas cityIds nulo, mantendo regionIds informado")
        void shouldReplaceOnlyNullCityIds() {
            final var regionId = RegionID.unique();

            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(regionId), null, null);

            assertEquals(Set.of(regionId), scope.regionIds());
            assertTrue(scope.cityIds().isEmpty());
        }
    }

    @Nested
    @DisplayName("Comportamento")
    class Behavior {

        @Test
        @DisplayName("deve ser incremental quando since informado")
        void shouldBeIncrementalWhenSinceIsPresent() {
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(), Set.of(), Instant.now());

            assertTrue(scope.isIncremental());
        }

        @Test
        @DisplayName("não deve ser incremental quando since é nulo")
        void shouldNotBeIncrementalWhenSinceIsNull() {
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(), Set.of(), null);

            assertFalse(scope.isIncremental());
        }

        @Test
        @DisplayName("deve indicar ausência de escopo quando regionIds e cityIds vazios")
        void shouldHaveNoScopeWhenBothEmpty() {
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(), Set.of(), null);

            assertTrue(scope.hasNoScope());
        }

        @Test
        @DisplayName("deve indicar presença de escopo quando há ao menos uma região")
        void shouldHaveScopeWhenHasRegion() {
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(RegionID.unique()), Set.of(), null);

            assertFalse(scope.hasNoScope());
        }

        @Test
        @DisplayName("deve indicar presença de escopo quando regionIds vazio mas há ao menos uma cidade")
        void shouldHaveScopeWhenRegionsEmptyButHasCity() {
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(), Set.of(CityID.unique()), null);

            assertFalse(scope.hasNoScope());
        }

        @Test
        @DisplayName("deve converter regionIds para UUIDs")
        void shouldConvertRegionIdsToUuids() {
            final var regionId = RegionID.unique();
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(regionId), Set.of(), null);

            assertEquals(Set.of(regionId.id()), scope.regionUuids());
        }

        @Test
        @DisplayName("deve converter cityIds para UUIDs")
        void shouldConvertCityIdsToUuids() {
            final var cityId = CityID.unique();
            final var scope = new SyncScope(
                    UserID.from(UUID.randomUUID()), Set.of(), Set.of(cityId), null);

            assertEquals(Set.of(cityId.id()), scope.cityUuids());
        }
    }
}
