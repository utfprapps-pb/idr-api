package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.infra.shared.testing.ApiIntegrationTest;
import br.gov.pr.idr.infra.shared.testing.ApiTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Autorização por propriedade, um caso permitido e um negado por tipo de escopo. As três propriedades do seed ficam
 * em Pato Branco ({@link #PATO_BRANCO}), da região {@link #PATO_BRANCO_REGION}.
 */
@ApiIntegrationTest
@DisplayName("Acesso às rotas de animais por escopo do usuário — Postgres real")
class AnimalAccessApiTest extends ApiTestSupport {

    private static final String PROPERTY = "c0000000-0000-0000-0000-000000000001";
    private static final String MIMOSA = "/properties/" + PROPERTY + "/animals/e0000000-0000-0000-0000-000000000001";
    private static final String PATO_BRANCO = "58ac2f17-d95a-457f-85a9-3db07f3f50e6";
    private static final String PATO_BRANCO_REGION = "77f65395-e7ed-4c6b-8678-4668bff690a2";
    private static final String APUCARANA = "0e747009-9abf-4253-89ab-cd1a208d983e";
    private static final String APUCARANA_REGION = "b109136b-2cef-432c-8deb-b1169ffc1de9";

    /** CPFs válidos (o domínio confere os dígitos ao carregar o usuário), um por usuário criado no teste. */
    private static final Map<String, String> CPFS = Map.of(
            "01", "123.456.781-43", "02", "234.567.892-54", "03", "345.678.913-09", "04", "456.789.124-45");

    /** Usuário com um único papel; {@code regionId}/{@code cityId} viram a permissão por localização, se informados. */
    private String userWith(final String suffix, final String role, final String regionId, final String cityId) {
        final var userId = "a0000000-0000-0000-0000-0000000001" + suffix;
        final var permissionId = "a0000000-0000-0000-0000-0000000002" + suffix;
        final var username = "acesso" + suffix + "@idr.com";
        sql("insert into users (id, active, cep, city_id, cpf, name, graduation_year, house_number, password, phone, "
                + "professional_register, street, username, created_at, updated_at) values ('" + userId + "', true, "
                + "'0000', '" + PATO_BRANCO + "', '" + CPFS.get(suffix) + "', 'Acesso " + suffix + "', '2000', '1', "
                + "'$2a$10$zVJ0LSYTBEhX7LEc1b2ND.07mCsRJo4NNfmfP6bQrUpH3DEBZuvA2', '0', '0', 'Rua', '" + username
                + "', NOW(), NOW())");
        sql("insert into users_permission (id, role, read_only, user_id) values ('" + permissionId + "', '" + role
                + "', false, '" + userId + "')");
        if (regionId != null) {
            sql("insert into permission_regions (permission_id, region_id) values ('" + permissionId + "', '"
                    + regionId + "')");
        }
        if (cityId != null) {
            sql("insert into permission_cities (permission_id, city_id) values ('" + permissionId + "', '" + cityId
                    + "')");
        }
        return username;
    }

    @Test
    @DisplayName("técnico vinculado acessa; desvinculado da propriedade recebe 403")
    void technicianNeedsToBeAssignedToTheProperty() throws Exception {
        perform(get(MIMOSA)).andExpect(status().isOk());

        sql("delete from property_technician where property_id = '" + PROPERTY + "'");

        perform(get(MIMOSA)).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("administrador acessa qualquer propriedade")
    void adminIsUnrestricted() throws Exception {
        performAs("admin@idr.com", get(MIMOSA)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("gerência municipal acessa a cidade da propriedade; de outra cidade recebe 403")
    void municipalManagerIsScopedByCity() throws Exception {
        performAs(userWith("01", "GERENCIA_MUNICIPAL", null, PATO_BRANCO), get(MIMOSA)).andExpect(status().isOk());
        performAs(userWith("02", "GERENCIA_MUNICIPAL", null, APUCARANA), get(MIMOSA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("gerência regional acessa a região da cidade da propriedade; de outra região recebe 403")
    void regionalManagerIsScopedByRegion() throws Exception {
        performAs(userWith("03", "GERENCIA_REGIONAL", PATO_BRANCO_REGION, null), get(MIMOSA))
                .andExpect(status().isOk());
        performAs(userWith("04", "GERENCIA_REGIONAL", APUCARANA_REGION, null), get(MIMOSA))
                .andExpect(status().isForbidden());
    }
}
