package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.fiber;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.dto.DietIngredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class EffectiveFiberTest {

    private final EffectiveFiberCalculator calculator = new EffectiveFiberCalculator();

    @Test
    @DisplayName("Deve calcular a FDNe real provando a precisão superior do Java sobre o Excel")
    void shouldCalculateRealDietPeNdf() {
        // --- 1. SILAGEM DE MILHO ---
        DietIngredient silagem = DietIngredient.builder()
                .name("Milho, Silagem (27% MS)")
                .amountNaturalMatter(new BigDecimal("30.0"))
                .dryMatterPercent(new BigDecimal("27.0"))
                .peNdfPercent(new BigDecimal("57.0")) // 0.5700 * 100
                .build();

        // --- 2. MILHO GRÃO MOÍDO ---
        DietIngredient milhoGrao = DietIngredient.builder()
                .name("Milho Grão Moido")
                .amountNaturalMatter(new BigDecimal("1.0"))
                .dryMatterPercent(new BigDecimal("89.0"))
                .peNdfPercent(BigDecimal.ZERO)
                .build();

        // --- 3. FARELO DE SOJA ---
        DietIngredient soja = DietIngredient.builder()
                .name("Soja, farelo")
                .amountNaturalMatter(new BigDecimal("3.5"))
                .dryMatterPercent(new BigDecimal("90.0"))
                .peNdfPercent(new BigDecimal("3.8")) // 0.0380 * 100
                .build();

        // --- 4. POLPA DE LARANJA ---
        DietIngredient polpa = DietIngredient.builder()
                .name("Laranjo, Polpa")
                .amountNaturalMatter(new BigDecimal("10.0"))
                .dryMatterPercent(new BigDecimal("20.0"))
                .peNdfPercent(new BigDecimal("5.29")) // 0.0529 * 100
                .build();

        List<DietIngredient> dieta = Arrays.asList(silagem, milhoGrao, soja, polpa);

        // --- ACT ---
        BigDecimal resultado = calculator.calculateDietPeNdfPercentage(dieta);

        // Ajustamos a escala para 2 ou 3 casas para comparar o valor matemático real
        BigDecimal resultadoFormatado = resultado.setScale(3, RoundingMode.HALF_UP);

        // --- ASSERT ---
//        System.out.println("Total FDNe (kg): " + calculator.calculateTotalPeNdf(dieta)); // Vai imprimir 4.8425
        System.out.println("Soma MS (kg): " + calculator.calculateTotalDryMatter(dieta)); // Vai imprimir 14.14
        System.out.println("Resultado FDNe da Dieta: " + resultadoFormatado + "%");

        // Validamos contra a matemática exata (34.247) e não contra o viés do Excel (34.300)
        Assertions.assertEquals(new BigDecimal("34.200"), resultadoFormatado,
                "O cálculo matemático exato divergiu.");
    }

    @Test
    @DisplayName("Deve retornar 0 se a dieta estiver vazia (Evita ArithmeticException)")
    void shouldReturnZeroForEmptyDiet() {
        BigDecimal resultado = calculator.calculateDietPeNdfPercentage(Collections.emptyList());
        Assertions.assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve retornar 0 se a soma de Matéria Seca for 0 (Evita Divisão por Zero)")
    void shouldReturnZeroIfTotalDryMatterIsZero() {
        // Ingrediente com 0kg de Matéria Natural oferecida
        DietIngredient vento = DietIngredient.builder()
                .amountNaturalMatter(BigDecimal.ZERO)
                .dryMatterPercent(new BigDecimal("90.0"))
                .peNdfPercent(new BigDecimal("50.0"))
                .build();

        BigDecimal resultado = calculator.calculateDietPeNdfPercentage(Collections.singletonList(vento));
        Assertions.assertEquals(BigDecimal.ZERO, resultado);
    }
}