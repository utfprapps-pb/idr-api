package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetabolizableEnergyTest {

    @Mock
    private DryMatterIntakeCalculator intakeMock;

    @InjectMocks
    private MetabolizableEnergyCalculator emCalculator;

    @Test
    @DisplayName("Deve calcular EM baseado na concentração de NDT")
    void shouldCalculateEmFromTdn() {

        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.LACTATING);

        when(intakeMock.calculatePredictedIntake(any())).thenReturn(20.0);

        BigDecimal exigenciaElSimulada = new BigDecimal("30.0");

        BigDecimal resultadoEm = emCalculator.calculateRequirement(ctx, exigenciaElSimulada);

        // VALIDAÇÃO
        Assertions.assertNotNull(resultadoEm);

        // Valor esperado baseado na conta:
        // EM = 1.01 * (70 * 0.04409) - 0.45 ≈ 2.667
        Assertions.assertEquals(2.667, resultadoEm.doubleValue(), 0.01);
    }

    @Test
    @DisplayName("Deve retornar zero quando IMS for zero")
    void shouldReturnZeroWhenIntakeIsZero() {

        AnimalContext ctx = new AnimalContext();

        when(intakeMock.calculatePredictedIntake(any())).thenReturn(0.0);

        BigDecimal exigenciaElSimulada = new BigDecimal("30.0");

        BigDecimal resultado = emCalculator.calculateRequirement(ctx, exigenciaElSimulada);

        // VALIDAÇÃO
        Assertions.assertEquals(BigDecimal.ZERO, resultado);
    }
}