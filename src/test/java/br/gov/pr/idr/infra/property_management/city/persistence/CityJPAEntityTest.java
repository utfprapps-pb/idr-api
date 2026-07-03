package br.gov.pr.idr.infra.property_management.city.persistence;

import br.gov.pr.idr.domain.property_management.city.City;
import br.gov.pr.idr.domain.property_management.city.vo.State;
import br.gov.pr.idr.domain.property_management.region.RegionID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CityJPAEntity")
class CityJPAEntityTest {

    @Test
    @DisplayName("deve converter entidade JPA em agregado de domínio")
    void shouldConvertToAggregate() {
        final var city = City.create("Curitiba", State.PR, RegionID.unique());
        final var entity = CityJPAEntity.from(city);

        final var aggregate = entity.toAggregate();

        assertEquals(city.getId(), aggregate.getId());
        assertEquals(city.getName(), aggregate.getName());
        assertEquals(city.getState(), aggregate.getState());
        assertEquals(city.getRegionId(), aggregate.getRegionId());
    }
}
