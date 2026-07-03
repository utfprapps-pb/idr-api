package br.gov.pr.idr.infra.property_management.city.models.update;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("UpdateCityRequest")
class UpdateCityRequestTest {

    @Test
    @DisplayName("deve criar request a partir dos dados informados")
    void shouldCreateRequestFromData() {
        final var regionId = UUID.randomUUID();
        final var name = "Curitiba";

        final var request = UpdateCityRequest.from(regionId, name);

        assertEquals(regionId, request.regionId());
        assertEquals(name, request.name());
    }
}
