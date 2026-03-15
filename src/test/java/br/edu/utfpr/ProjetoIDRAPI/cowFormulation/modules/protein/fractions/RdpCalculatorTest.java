package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RdpCalculatorTest {

    @Mock
    private DryMatterIntakeCalculator intakeMock;

    // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
//    @InjectMocks
//    private RdpRequirementCalculator rdpCalculator;

    @Test
    @DisplayName("Deve calcular PDR para Vaca em Lactação usando regressão")
    void shouldCalculateRdpForLactatingCow() {
        // CENÁRIO COMPLETO (Inputs que afetam a regressão)
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.LACTATING)
                .bodyWeight(new BigDecimal("600"))        // Peso afeta
                .milkYield(new BigDecimal("30"))          // Leite afeta
                .milkFatPct(new BigDecimal("3.5"))        // Gordura afeta
                .milkProteinPct(new BigDecimal("3.1"))    // Proteína afeta
                .balanceDate(LocalDate.now())
                .nextVisitDate(LocalDate.now().plusDays(30))
                .weightChangeGoal(BigDecimal.ZERO)
                .build();

        // Mockamos o IMS em 20kg.
        // A fórmula final é: Regressão(% da dieta) * IMS
        when(intakeMock.calculatePredictedIntake(any())).thenReturn(20.0);

        // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
        // AÇÃO
//        BigDecimal pdrTotal = rdpCalculator.calculateRequirement(ctx);

        // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
        // VALIDAÇÃO
//        System.out.println("PDR Calculado (kg/dia): " + pdrTotal);

        // Dica para o André:
        // 1. Rode esse teste uma vez.
        // 2. Pegue o valor impresso no console.
        // 3. Valide na planilha Excel manualmente se bate.
        // 4. Se bater, fixe o valor aqui no assert para garantir regressão futura.

        // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
//        Assertions.assertNotNull(pdrTotal);
//        Assertions.assertTrue(pdrTotal.compareTo(BigDecimal.ZERO) > 0, "PDR deve ser maior que zero");
    }

    @Test
    @DisplayName("Deve retornar Zero se não houver consumo (IMS=0)")
    void shouldReturnZeroIfIntakeIsZero() {
        AnimalContext ctx = new AnimalContext();
        when(intakeMock.calculatePredictedIntake(any())).thenReturn(0.0);

        // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
//        BigDecimal result = rdpCalculator.calculateRequirement(ctx);

//        Assertions.assertEquals(BigDecimal.ZERO, result);
    }
}