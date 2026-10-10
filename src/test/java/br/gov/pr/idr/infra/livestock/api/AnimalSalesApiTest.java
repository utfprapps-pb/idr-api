package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.infra.shared.testing.ApiIntegrationTest;
import br.gov.pr.idr.infra.shared.testing.ApiTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApiIntegrationTest
@DisplayName("API de vendas — ponta a ponta com Postgres real")
class AnimalSalesApiTest extends ApiTestSupport {

    private static final String CHARMOSA = "/properties/c0000000-0000-0000-0000-000000000001/animals/e0000000-0000-0000-0000-000000000002";

    @Test
    @DisplayName("grava e devolve peso, valor e os enums em português que o front traduz")
    void shouldRoundTripSale() throws Exception {
        final var created = mockMvc.perform(authenticated(post(CHARMOSA + "/sales").content("""
                        {"dateSale":"2024-06-10","reason":"DESCARTE","value":1500.00,
                         "destination":"ABATE","weight":450.50}""")))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(authenticated(get(CHARMOSA + "/sales/" + idOf(created))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateSale").value("2024-06-10"))
                .andExpect(jsonPath("$.reason").value("DESCARTE"))
                .andExpect(jsonPath("$.destination").value("ABATE"))
                .andExpect(jsonPath("$.value").value(1500.0))
                .andExpect(jsonPath("$.weight").value(450.5));
    }
}
