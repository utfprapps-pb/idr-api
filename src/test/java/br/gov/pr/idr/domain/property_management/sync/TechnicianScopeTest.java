package br.gov.pr.idr.domain.property_management.sync;

import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import br.gov.pr.idr.domain.property_management.sync.technician.TechnicianScope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TechnicianScope — Value Object")
class TechnicianScopeTest {

    @Nested
    @DisplayName("Null-safety dos conjuntos")
    class NullSafety {

        @Test
        @DisplayName("deve substituir regionIds e cityIds nulos por conjuntos vazios")
        void shouldReplaceBothNullSetsWithEmptySets() {
            final var scope = new TechnicianScope(null, null);

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

            final var scope = new TechnicianScope(Set.of(regionId), Set.of(cityId));

            assertEquals(Set.of(regionId), scope.regionIds());
            assertEquals(Set.of(cityId), scope.cityIds());
        }

        @Test
        @DisplayName("deve substituir apenas regionIds nulo, mantendo cityIds informado")
        void shouldReplaceOnlyNullRegionIds() {
            final var cityId = CityID.unique();

            final var scope = new TechnicianScope(null, Set.of(cityId));

            assertTrue(scope.regionIds().isEmpty());
            assertEquals(Set.of(cityId), scope.cityIds());
        }

        @Test
        @DisplayName("deve substituir apenas cityIds nulo, mantendo regionIds informado")
        void shouldReplaceOnlyNullCityIds() {
            final var regionId = RegionID.unique();

            final var scope = new TechnicianScope(Set.of(regionId), null);

            assertEquals(Set.of(regionId), scope.regionIds());
            assertTrue(scope.cityIds().isEmpty());
        }
    }
}
