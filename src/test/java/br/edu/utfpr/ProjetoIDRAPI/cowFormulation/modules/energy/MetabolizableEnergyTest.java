package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetabolizableEnergyTest {

    @Mock
    private DryMatterIntakeCalculator intakeMock;

    // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
    @InjectMocks
    private MetabolizableEnergyCalculator emCalculator;

    @Test
    @DisplayName("Deve calcular EM baseado na concentração de NDT")
    void shouldCalculateEmFromTdn() {
        // CENÁRIO
        // Suponha que temos uma exigência de Energia Líquida que resulta
        // em uma necessidade de NDT. Vamos testar a conversão final.
        // Fórmula Base: EM (Mcal/kg) = 1.01 * ED - 0.45
        // Onde ED = NDT% * 0.04409

        // Vamos simular:
        // IMS = 20kg
        // NDT da Dieta = 70%
        // ED = 70 * 0.04409 = 3.0863 Mcal/kg
        // EM Esperada = 1.01 * 3.0863 - 0.45 = 2.667 Mcal/kg

        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.LACTATING);

        // Simulamos que o consumo é 20kg (importante para diluição energética)
        when(intakeMock.calculatePredictedIntake(any())).thenReturn(20.0);

        // Supondo que o método da Dayse receba a Exigência de EL ou NDT
        // Aqui estou abstraindo a entrada, assumindo que ela calculou NDT interno como 70%
        // Se o método pedir EL como input, passe um valor que force o NDT a 70%.
        BigDecimal exigenciaElSimulada = new BigDecimal("30.0"); // Exemplo arbitrário

        // TODO: DESCOMENTAR QUANDO IMPLEMENTADO
        // AÇÃO
        // Adapte o nome do método conforme a implementação real da Dayse
        BigDecimal resultadoEm = emCalculator.calculateRequirement(ctx, exigenciaElSimulada);

        // VALIDAÇÃO
        // Verifique se o mock foi chamado
        Assertions.assertNotNull(resultadoEm);
        // Exemplo de assert com margem de erro
         Assertions.assertEquals(2.667, resultadoEm.doubleValue(), 0.01);
    }
}