package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.infra.shared.testing.ApiIntegrationTest;
import br.gov.pr.idr.infra.shared.testing.ApiTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApiIntegrationTest
@DisplayName("Busca de propriedades pelo escopo do usuário autenticado — Postgres real")
class PropertySearchApiTest extends ApiTestSupport {

    @Test
    @DisplayName("técnico vê só as propriedades a que está vinculado; administrador vê todas")
    void shouldScopeSearchByTheAuthenticatedUser() throws Exception {
        sql("delete from property_technician where property_id = 'c0000000-0000-0000-0000-000000000003'");

        perform(get("/v1/properties/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2));
        performAs("admin@idr.com", get("/v1/properties/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3));
    }
}
