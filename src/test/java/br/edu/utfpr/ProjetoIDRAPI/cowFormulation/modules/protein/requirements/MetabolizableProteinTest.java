package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirements;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions.interfaces.PndrServiceInterface;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirement.MetabolizableProteinCalculator;
import br.edu.utfpr.ProjetoIDRAPI.enums.AnimalSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class MetabolizableProteinTest {

    //    @Mock // Cria uma versão falsa do calculador de IMS
    private DryMatterIntakeCalculator intakeMock;

    @Mock // Cria uma versão falsa do serviço de PNDR (interface)
    private PndrServiceInterface pndrMock;

    @InjectMocks // Injeta os mocks dentro da sua classe de cálculo
    private MetabolizableProteinCalculator pmCalculator;

    @Test
    @DisplayName("Deve calcular PM Fecal corretamente isolando as dependências")
    void shouldCalculatePmRequirement() {
        AnimalContext ctx = getAnimalContext();

        // Mockito: "Quando alguém pedir o IMS, retorne xxKg"
//        when(intakeMock.calculatePredictedIntake(any())).thenReturn(17.92);

        // Mockito: "Quando alguém pedir o PNDR, retorne xxkg"
//        when(pndrMock.calculatePndrSupply(any())).thenReturn(new BigDecimal("1.14"));

        // --- ACT (Executar) ---
        BigDecimal resultadoPm = pmCalculator.calculateRequirement(ctx);

        // --- ASSERT (Verificar) ---
        System.out.println("Resultado PM Calculado: " + resultadoPm);
        System.out.println("PM REAL: 3.449 ou 3,45");

        Assertions.assertNotNull(resultadoPm);
        Assertions.assertEquals(new BigDecimal("3.449"), resultadoPm);
    }

    @Test
    @DisplayName("Deve calcular IMS corretamente")
    void shouldCalculateIMSCorrectly() {
        AnimalContext ctx = getAnimalContext();
        DryMatterIntakeCalculator intakeCalculator = new DryMatterIntakeCalculator();
        double ims = intakeCalculator.calculatePredictedIntake(ctx);
        System.out.println("IMS: " + ims);
    }

    private static AnimalContext getAnimalContext() {
        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.LACTATING);
        ctx.setBodyWeight(new BigDecimal("450"));
        ctx.setWeightChangeGoal(BigDecimal.ZERO);
        ctx.setBalanceDate(LocalDate.of(2025, 8, 29));
        ctx.setLactationNumber(33);
        ctx.setMilkFatPct(new BigDecimal("3.93"));
        ctx.setMilkProteinPct(new BigDecimal("3.32"));
        ctx.setSize(AnimalSize.MEDIUM);
        ctx.setMilkYield(new BigDecimal("33.6"));
        ctx.setMilkYieldForCalculations(new BigDecimal("33.6"));
        ctx.setEcc(new BigDecimal("3.50"));
        ctx.setNextVisitDate(LocalDate.of(2025, 9, 20));
        ctx.setAmbientTemperature(new BigDecimal("35"));
        return ctx;
    }
}