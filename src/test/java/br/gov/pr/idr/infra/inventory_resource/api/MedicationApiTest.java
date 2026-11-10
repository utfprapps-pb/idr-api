package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.infra.shared.testing.ApiIntegrationTest;
import br.gov.pr.idr.infra.shared.testing.ApiTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.matchesPattern;
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
        return """
                {"productId":%s,"applicationDate":"2024-06-10","appliedDose":"10 ml","applicationWay":"Oral"}"""
                .formatted(productId == null ? "null" : '"' + productId + '"');
    }

    @Test
    @DisplayName("exige o produto (o front ainda não envia productId)")
    void shouldRequireProduct() throws Exception {
        perform(post(MIMOSA + "/medications").content(medication(null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(matchesPattern("[0-9]{4}-[0-9]{2}-[0-9]{2}T.*Z")));
    }

    @Test
    @DisplayName("troca o produto no PUT e nunca o apaga")
    void shouldKeepProductAcrossUpdates() throws Exception {
        final var created = perform(post(MIMOSA + "/medications").content(medication(PRODUCT_1)))
                .andExpect(status().isOk())
                .andReturn();
        final var path = MIMOSA + "/medications/" + idOf(created);

        perform(put(path).content(medication(PRODUCT_2)))
                .andExpect(status().isOk());
        perform(put(path).content(medication(null)))
                .andExpect(status().isBadRequest());

        perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_2))
                .andExpect(jsonPath("$.applicationDate").value("2024-06-10"));
    }
}
