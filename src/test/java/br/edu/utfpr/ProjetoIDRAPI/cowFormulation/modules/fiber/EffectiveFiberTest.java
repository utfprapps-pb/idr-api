package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.fiber;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.dto.DietIngredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class EffectiveFiberTest {

    private final EffectiveFiberCalculator calculator = new EffectiveFiberCalculator();

    @Test
    @DisplayName("Deve calcular a média ponderada de FDNe na dieta corretamente")
    void shouldCalculateDietPeNdfPercentage() {
        // CENÁRIO:
        // 1. Silagem de Milho: 10kg MN, 35% MS (3.5kg MS), 50% FDNe (1.75kg Fibra)
        // 2. Farelo de Soja:   2kg MN,  88% MS (1.76kg MS), 5% FDNe  (0.088kg Fibra)

        // Total MS = 3.5 + 1.76 = 5.26 kg
        // Total Fibra = 1.75 + 0.088 = 1.838 kg
        // Resultado Esperado = (1.838 / 5.26) * 100 = 34.94%

        DietIngredient silagem = DietIngredient.builder()
                .name("Silagem")
                .amountNaturalMatter(new BigDecimal("10"))
                .dryMatterPercent(new BigDecimal("35"))
                .peNdfPercent(new BigDecimal("50"))
                .build();

        DietIngredient soja = DietIngredient.builder()
                .name("Soja")
                .amountNaturalMatter(new BigDecimal("2"))
                .dryMatterPercent(new BigDecimal("88"))
                .peNdfPercent(new BigDecimal("5"))
                .build();

        List<DietIngredient> dieta = Arrays.asList(silagem, soja);

        // AÇÃO
        BigDecimal resultado = calculator.calculateDietPeNdfPercentage(dieta);

        // VALIDAÇÃO
        System.out.println("FDNe Calculado: " + resultado + "%");
        // Aceitamos arredondamento na primeira casa decimal
        Assertions.assertEquals(0, new BigDecimal("34.9").compareTo(resultado));
    }

    @Test
    @DisplayName("Deve retornar Zero se a lista estiver vazia para evitar divisão por zero")
    void shouldReturnZeroForEmptyDiet() {
        BigDecimal result = calculator.calculateDietPeNdfPercentage(Collections.emptyList());
        Assertions.assertEquals(BigDecimal.ZERO, result);
    }
}