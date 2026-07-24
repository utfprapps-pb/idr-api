package br.gov.pr.idr.domain.property_management.property;

import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PropertySearchScope — Value Object")
class PropertySearchScopeTest {

    @Nested
    @DisplayName("Null-safety dos conjuntos")
    class NullSafety {

        @Test
        @DisplayName("deve substituir regionIds e cityIds nulos por conjuntos vazios")
        void shouldReplaceBothNullSetsWithEmptySets() {
            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.BY_LOCATION, null, null, null);

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

            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.BY_LOCATION, Set.of(regionId), Set.of(cityId), null);

            assertEquals(Set.of(regionId), scope.regionIds());
            assertEquals(Set.of(cityId), scope.cityIds());
        }

        @Test
        @DisplayName("deve substituir apenas regionIds nulo, mantendo cityIds informado")
        void shouldReplaceOnlyNullRegionIds() {
            final var cityId = CityID.unique();

            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.BY_LOCATION, null, Set.of(cityId), null);

            assertTrue(scope.regionIds().isEmpty());
            assertEquals(Set.of(cityId), scope.cityIds());
        }

        @Test
        @DisplayName("deve substituir apenas cityIds nulo, mantendo regionIds informado")
        void shouldReplaceOnlyNullCityIds() {
            final var regionId = RegionID.unique();

            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.BY_LOCATION, Set.of(regionId), null, null);

            assertEquals(Set.of(regionId), scope.regionIds());
            assertTrue(scope.cityIds().isEmpty());
        }
    }

    @Nested
    @DisplayName("Factories")
    class Factories {

        @Test
        @DisplayName("deve criar escopo irrestrito sem regiões, cidades ou técnico")
        void shouldCreateUnrestrictedScope() {
            final var scope = PropertySearchScope.unrestricted();

            assertEquals(PropertySearchScope.Type.UNRESTRICTED, scope.type());
            assertTrue(scope.hasNoLocation());
            assertNull(scope.technicianId());
        }

        @Test
        @DisplayName("deve criar escopo por localização com regiões e cidades")
        void shouldCreateScopeByLocation() {
            final var regionId = RegionID.unique();
            final var cityId = CityID.unique();

            final var scope = PropertySearchScope.byLocation(Set.of(regionId), Set.of(cityId));

            assertEquals(PropertySearchScope.Type.BY_LOCATION, scope.type());
            assertFalse(scope.hasNoLocation());
        }

        @Test
        @DisplayName("deve criar escopo por técnico sem regiões ou cidades")
        void shouldCreateScopeByTechnician() {
            final var technicianId = UserID.from(UUID.randomUUID());

            final var scope = PropertySearchScope.byTechnician(technicianId);

            assertEquals(PropertySearchScope.Type.BY_TECHNICIAN, scope.type());
            assertEquals(technicianId, scope.technicianId());
            assertTrue(scope.hasNoLocation());
        }
    }

    @Nested
    @DisplayName("hasNoLocation()")
    class HasNoLocation {

        @Test
        @DisplayName("deve retornar true quando regionIds e cityIds estão vazios")
        void shouldReturnTrueWhenBothEmpty() {
            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.UNRESTRICTED, Set.of(), Set.of(), null);

            assertTrue(scope.hasNoLocation());
        }

        @Test
        @DisplayName("deve retornar false quando há ao menos uma região")
        void shouldReturnFalseWhenHasRegion() {
            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.BY_LOCATION, Set.of(RegionID.unique()), Set.of(), null);

            assertFalse(scope.hasNoLocation());
        }

        @Test
        @DisplayName("deve retornar false quando há ao menos uma cidade")
        void shouldReturnFalseWhenHasCity() {
            final var scope = new PropertySearchScope(
                    PropertySearchScope.Type.BY_LOCATION, Set.of(), Set.of(CityID.unique()), null);

            assertFalse(scope.hasNoLocation());
        }
    }
}
