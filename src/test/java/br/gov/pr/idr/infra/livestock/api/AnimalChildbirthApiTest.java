package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.infra.shared.testing.ApiIntegrationTest;
import br.gov.pr.idr.infra.shared.testing.ApiTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApiIntegrationTest
@DisplayName("API de partos — ponta a ponta com Postgres real")
class AnimalChildbirthApiTest extends ApiTestSupport {

    private static final String MIMOSA = "/properties/c0000000-0000-0000-0000-000000000001/animals/e0000000-0000-0000-0000-000000000001";
    private static final String HOLANDES = "6f8b8953-72ab-4525-afdf-2ee4d0561b5e";
    private static final String CHARMOSA = "e0000000-0000-0000-0000-000000000002";
    private static final String CHARMOSA_PATH = "/properties/c0000000-0000-0000-0000-000000000001/animals/" + CHARMOSA;
    private static final String ESTRELA_OTHER_PROPERTY = "e0000000-0000-0000-0000-000000000004";

    private static String childbirth(final String calfId) {
        return """
                {"childbirthDate":"2024-06-10","gender":"FEMALE","weight":35.50,"condition":"ALIVE",
                 "breedId":"%s","calfId":%s}""".formatted(HOLANDES, calfId == null ? "null" : '"' + calfId + '"');
    }

    @Test
    @DisplayName("registra parto sem bezerro e devolve o nome da raça na leitura")
    void shouldRecordChildbirthWithoutCalf() throws Exception {
        final var created = perform(post(MIMOSA + "/childbirths").content(childbirth(null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calfId").value(nullValue()))
                .andReturn();

        perform(get(MIMOSA + "/childbirths/" + idOf(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.childbirthDate").value("2024-06-10"))
                .andExpect(jsonPath("$.weight").value(35.5))
                .andExpect(jsonPath("$.breedName").value("Holandês"))
                .andExpect(jsonPath("$.calfIdentifier").value(nullValue()));
    }

    @Test
    @DisplayName("vincula um bezerro da mesma propriedade e devolve o identificador dele")
    void shouldLinkCalfOfTheSameProperty() throws Exception {
        final var created = perform(post(MIMOSA + "/childbirths").content(childbirth(null)))
                .andExpect(status().isOk())
                .andReturn();

        perform(put(MIMOSA + "/childbirths/" + idOf(created)).content(childbirth(CHARMOSA)))
                .andExpect(status().isOk());

        perform(get(MIMOSA + "/childbirths/" + idOf(created)))
                .andExpect(jsonPath("$.calfId").value(CHARMOSA))
                .andExpect(jsonPath("$.calfIdentifier").value("Charmosa"));
    }

    @Test
    @DisplayName("não devolve o parto pelo caminho de outro animal")
    void shouldNotExposeChildbirthUnderAnotherAnimal() throws Exception {
        final var created = perform(post(MIMOSA + "/childbirths").content(childbirth(null)))
                .andExpect(status().isOk())
                .andReturn();

        perform(get(CHARMOSA_PATH + "/childbirths/" + idOf(created)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("recusa bezerro de outra propriedade")
    void shouldRejectCalfFromAnotherProperty() throws Exception {
        perform(post(MIMOSA + "/childbirths").content(childbirth(ESTRELA_OTHER_PROPERTY)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("recusa animal que não pertence à propriedade do caminho")
    void shouldRejectAnimalOutsideTheProperty() throws Exception {
        perform(get(
                        "/properties/c0000000-0000-0000-0000-000000000002/animals/e0000000-0000-0000-0000-000000000001/childbirths/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ordenação por campo desconhecido é erro do cliente (400), sem expor a entidade")
    void shouldRejectUnknownSortField() throws Exception {
        perform(get(MIMOSA + "/childbirths/search?sort=foo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campo de ordenação inválido"));
    }

    @Test
    @DisplayName("exige autenticação")
    void shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get(MIMOSA + "/childbirths/search"))
                .andExpect(status().isForbidden());
    }
}
