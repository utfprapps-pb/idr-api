package br.gov.pr.idr.application.inventory_resource.medication.shared;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("MedicationProductPolicy — produto digitado")
class MedicationProductPolicyTest {

    @Mock ProductGateway productGateway;
    @Mock ActivePrincipleGateway activePrincipleGateway;

    private MedicationProductPolicy.MedicationProduct typed(final String productName, final String activePrinciple) {
        return MedicationProductPolicy.resolve(productGateway, activePrincipleGateway, null, productName,
                activePrinciple);
    }

    @Test
    @DisplayName("remove os espaços das pontas do produto e do princípio ativo digitados")
    void shouldTrimTypedFields() {
        final var product = typed("  Vermífugo caseiro ", "\tIvermectina  ");

        assertNull(product.productId());
        assertEquals("Vermífugo caseiro", product.productName());
        assertEquals("Ivermectina", product.activePrinciple());
        verifyNoInteractions(productGateway, activePrincipleGateway);
    }

    @Test
    @DisplayName("produto e princípio ativo em branco viram null")
    void shouldTurnBlankTypedFieldsIntoNull() {
        final var product = typed("   ", "");

        assertNull(product.productName());
        assertNull(product.activePrinciple());
    }

    @Test
    @DisplayName("produto e princípio ativo nulos continuam nulos")
    void shouldKeepNullTypedFields() {
        final var product = typed(null, null);

        assertNull(product.productName());
        assertNull(product.activePrinciple());
    }
}
