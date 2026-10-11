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
@DisplayName("API de mortes — ponta a ponta com Postgres real")
class AnimalDeathApiTest extends ApiTestSupport {

    private static final String LEITOSA = "/properties/c0000000-0000-0000-0000-000000000001/animals/e0000000-0000-0000-0000-000000000003";
    /** Already dead in the seed. */
    private static final String ESTRELA = "/properties/c0000000-0000-0000-0000-000000000002/animals/e0000000-0000-0000-0000-000000000004";
    private static final String DEATH = """
            {"reason":"Pneumonia","deathDate":"2024-06-10"}""";

    @Test
    @DisplayName("registra a morte e devolve data e causa na leitura")
    void shouldRecordDeath() throws Exception {
        final var created = perform(post(LEITOSA + "/deaths").content(DEATH))
                .andExpect(status().isOk())
                .andReturn();

        perform(get(LEITOSA + "/deaths/" + idOf(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Pneumonia"))
                .andExpect(jsonPath("$.deathDate").value("2024-06-10"));
    }

    @Test
    @DisplayName("recusa uma segunda morte para o mesmo animal")
    void shouldRejectSecondDeath() throws Exception {
        perform(post(ESTRELA + "/deaths").content(DEATH))
                .andExpect(status().isBadRequest());

        perform(get(ESTRELA + "/deaths/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
