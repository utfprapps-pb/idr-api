package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
     //Dados retirados da vaca Tiririca na planilha versão 2024
@ExtendWith(MockitoExtension.class)
class NetEnergyCalculatorTest {

    @Mock
    private EnergyRequirementCalculator energyRequirementMock;

    @Mock
    private DryMatterIntakeCalculator intakeMock;

    @InjectMocks
    private NetEnergyCalculator netEnergyCalculator;

    @Test
    @DisplayName("Deve calcular NE usando dados reais da planilha")
    void shouldCalculateNetEnergyFromSpreadsheet() {

        // CENÁRIO (dados vaca tiririca)
        AnimalContext ctx = new AnimalContext();

        when(energyRequirementMock.calculateELMaintenance(any(), anyDouble(), anyInt()))
                .thenReturn(new BigDecimal("8.72"));

        when(energyRequirementMock.calculateELLactation(any()))
                .thenReturn(new BigDecimal("17.43"));

        when(energyRequirementMock.calculateELGestation(any(), anyDouble()))
                .thenReturn(BigDecimal.ZERO);

        when(energyRequirementMock.calculateELWeightChange(any(), anyDouble()))
                .thenReturn(BigDecimal.ZERO);

        when(intakeMock.calculatePredictedIntake(any()))
                .thenReturn(15.87);

        // AÇÃO
        BigDecimal result = netEnergyCalculator.calculateNetEnergyPerKg(
                ctx,
                0.0, // não impacta (mockado)
                0,
                0.0,
                0.0
        );

        // VALIDAÇÃO
        // NE = 26.15 / 15.87 = 1.647
        Assertions.assertEquals(new BigDecimal("1.647"), result);
    }
}