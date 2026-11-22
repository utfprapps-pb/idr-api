package br.gov.pr.idr.infra.inventory_resource.api;

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
@DisplayName("API de medicações — ponta a ponta com Postgres real")
class MedicationApiTest extends ApiTestSupport {

    private static final String MIMOSA = "/properties/c0000000-0000-0000-0000-000000000001/animals/e0000000-0000-0000-0000-000000000001";
    private static final String PRODUCT_1 = "604754e8-ec60-49cd-8579-fa831e5675c7";
    private static final String PRODUCT_2 = "ee1cf7e2-5211-4688-82a3-62248ea417d2";

    private static String medication(final String productId) {
        return medication(productId, null);
    }

    private static String medication(final String productId, final String productName) {
        return """
                {"productId":%s,"productName":%s,"activePrinciple":"Digitado pelo cliente",\
                "applicationDate":"2024-06-10","appliedDose":"10 ml","applicationWay":"Oral"}"""
                .formatted(quoted(productId), quoted(productName));
    }

    private static String quoted(final String value) {
        return value == null ? "null" : '"' + value + '"';
    }

    @Test
    @DisplayName("exige o nome do produto quando não há produto do catálogo")
    void shouldRequireProductNameWithoutCatalogProduct() throws Exception {
        perform(post(MIMOSA + "/medications").content(medication(null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:idr:problem:validacao"))
                .andExpect(jsonPath("$.errors[0].message").value("O produto da medicação é obrigatório"));
    }

    @Test
    @DisplayName("com produto do catálogo grava o nome e o princípio ativo do catálogo")
    void shouldSnapshotCatalogProduct() throws Exception {
        final var created = perform(post(MIMOSA + "/medications").content(medication(PRODUCT_1, "Outro nome")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_1))
                .andExpect(jsonPath("$.productName").value("Product 1"))
                .andExpect(jsonPath("$.activePrinciple").value("Active principle 2"))
                .andReturn();

        perform(get(MIMOSA + "/medications/" + idOf(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Product 1"))
                .andExpect(jsonPath("$.activePrinciple").value("Active principle 2"));
    }

    @Test
    @DisplayName("sem produto do catálogo grava o produto digitado")
    void shouldCreateWithTypedProduct() throws Exception {
        final var created = perform(post(MIMOSA + "/medications").content(medication(null, "Vermífugo caseiro")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(nullValue()))
                .andExpect(jsonPath("$.productName").value("Vermífugo caseiro"))
                .andExpect(jsonPath("$.activePrinciple").value("Digitado pelo cliente"))
                .andReturn();

        perform(get(MIMOSA + "/medications/" + idOf(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(nullValue()))
                .andExpect(jsonPath("$.productName").value("Vermífugo caseiro"));
    }

    @Test
    @DisplayName("produto do catálogo inexistente devolve 400")
    void shouldRejectUnknownCatalogProduct() throws Exception {
        perform(post(MIMOSA + "/medications").content(medication("00000000-0000-0000-0000-0000000000ff", "X")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("troca o produto no PUT, desvincula do catálogo e exige o nome")
    void shouldSwitchProductAcrossUpdates() throws Exception {
        final var created = perform(post(MIMOSA + "/medications").content(medication(PRODUCT_1)))
                .andExpect(status().isOk())
                .andReturn();
        final var path = MIMOSA + "/medications/" + idOf(created);

        perform(put(path).content(medication(PRODUCT_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Product 2"))
                .andExpect(jsonPath("$.activePrinciple").value("Active principle 3"));
        perform(put(path).content(medication(null)))
                .andExpect(status().isBadRequest());

        perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_2))
                .andExpect(jsonPath("$.applicationDate").value("2024-06-10"));

        perform(put(path).content(medication(null, "Vermífugo caseiro")))
                .andExpect(status().isOk());
        perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(nullValue()))
                .andExpect(jsonPath("$.productName").value("Vermífugo caseiro"))
                .andExpect(jsonPath("$.activePrinciple").value("Digitado pelo cliente"));
    }
}
