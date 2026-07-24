package br.gov.pr.idr.infra.property_management.region.persistence;

import br.gov.pr.idr.domain.property_management.region.Region;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("RegionJPAEntity")
class RegionJPAEntityTest {

    @Test
    @DisplayName("deve converter entidade JPA em agregado de domínio")
    void shouldConvertToAggregate() {
        final var region = Region.create("Região Sul");
        final var entity = RegionJPAEntity.from(region);

        final var aggregate = entity.toAggregate();

        assertEquals(region.getId(), aggregate.getId());
        assertEquals(region.getDescription(), aggregate.getDescription());
    }
}
