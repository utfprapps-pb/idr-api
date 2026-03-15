package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirements;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirement.MetabolizableProteinCalculator;
import br.edu.utfpr.ProjetoIDRAPI.enums.AnimalSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class MetabolizableProteinTest {

    //    @Mock // Cria uma versão falsa do calculador de IMS
//    private DryMatterIntakeCalculator intakeMock;

//    @Mock // Cria uma versão falsa do serviço de PNDR (interface)
//    private RumenUndegradableProteinCalculator pndrMock;

    @InjectMocks // Injeta os mocks dentro da sua classe de cálculo
    private MetabolizableProteinCalculator pmCalculator;

    @Test
    @DisplayName("LACTATING: Deve calcular PM Fecal corretamente isolando as dependências")
    void shouldCalculatePmRequirementForLactatingCow() {
        AnimalContext ctx = getAnimalLactatingContext();

        // Mockito: "Quando alguém pedir o IMS, retorne xxKg"
//        when(intakeMock.calculatePredictedIntake(any())).thenReturn(17.92);

        // Mockito: "Quando alguém pedir o PNDR, retorne xxkg"
//        when(pndrMock.calculatePndrSupply(any())).thenReturn(new BigDecimal("1.14"));

        // --- ACT (Executar) ---
        BigDecimal resultadoPm = pmCalculator.calculateRequirement(ctx);

        // --- ASSERT (Verificar) ---
        System.out.println("Resultado PM Calculado: " + resultadoPm);
        System.out.println("PM REAL: 3.449 ou 3,45"); // PLANILHA COM ARREDONDAMENTO

        Assertions.assertNotNull(resultadoPm);
//        Assertions.assertEquals(new BigDecimal("3.449"), resultadoPm);
        Assertions.assertEquals(new BigDecimal("3.453"), resultadoPm); // 3.453 SEM ARREDONDAMENTO DA PLANILHA
    }

    @Test
    @DisplayName("DRY: Deve calcular PM Fecal corretamente isolando as dependências")
    void shouldCalculatePmRequirementForDryCow() {
        AnimalContext ctx = getAnimalDryContext();

        // Mockito: "Quando alguém pedir o IMS, retorne xxKg"
//        when(intakeMock.calculatePredictedIntake(any())).thenReturn(17.92);

        // Mockito: "Quando alguém pedir o PNDR, retorne xxkg"
//        when(pndrMock.calculatePndrSupply(any())).thenReturn(new BigDecimal("1.14"));

        // --- ACT (Executar) ---
        BigDecimal resultadoPm = pmCalculator.calculateRequirement(ctx);

        // --- ASSERT (Verificar) ---
        System.out.println("Resultado PM Calculado: " + resultadoPm);
        System.out.println("PM REAL: 0.707"); // PLANILHA COM ARREDONDAMENTO

        Assertions.assertNotNull(resultadoPm);
        Assertions.assertEquals(new BigDecimal("0.707"), resultadoPm);
//        Assertions.assertEquals(new BigDecimal("3.453"), resultadoPm); // 3.453 SEM ARREDONDAMENTO DA PLANILHA
    }

    @Test
    @DisplayName("DRY 2: Deve calcular PM Fecal corretamente isolando as dependências")
    void shouldCalculatePmRequirementForDryCow2() {
        AnimalContext ctx = getAnimalDryContext2();

        // Mockito: "Quando alguém pedir o IMS, retorne xxKg"
//        when(intakeMock.calculatePredictedIntake(any())).thenReturn(17.92);

        // Mockito: "Quando alguém pedir o PNDR, retorne xxkg"
//        when(pndrMock.calculatePndrSupply(any())).thenReturn(new BigDecimal("1.14"));

        // --- ACT (Executar) ---
        BigDecimal resultadoPm = pmCalculator.calculateRequirement(ctx);

        // --- ASSERT (Verificar) ---
        System.out.println("Resultado PM Calculado: " + resultadoPm);
        System.out.println("PM REAL: 0.818 ou 0.82"); // PLANILHA COM ARREDONDAMENTO

        Assertions.assertNotNull(resultadoPm);
        Assertions.assertEquals(new BigDecimal("0.818"), resultadoPm);
//        Assertions.assertEquals(new BigDecimal("3.453"), resultadoPm); // 3.453 SEM ARREDONDAMENTO DA PLANILHA
    }







    @Test
    @DisplayName("LACTATING: Deve calcular IMS corretamente")
    void shouldCalculateIMSCorrectlyForLactatingCow() {
        AnimalContext ctx = getAnimalLactatingContext();
        DryMatterIntakeCalculator intakeCalculator = new DryMatterIntakeCalculator();
        double ims = intakeCalculator.calculatePredictedIntake(ctx);
        System.out.println("IMS: " + ims);

        Assertions.assertTrue(ims > 0);
//        Assertions.assertEquals(17.92, ims); // RESULTADO DA PLANILHA
        Assertions.assertEquals(17.99, ims); // RESULTADO DO CÁLCULO SEM ARREDONDAMENTO
    }

    @Test
    @DisplayName("DRY: Deve calcular IMS corretamente")
    void shouldCalculateIMSCorrectlyForDryCow() {
        AnimalContext ctx = getAnimalDryContext();
        DryMatterIntakeCalculator intakeCalculator = new DryMatterIntakeCalculator();
        double ims = intakeCalculator.calculatePredictedIntake(ctx);
        System.out.println("IMS: " + ims);
        System.out.println("IMS Planilha: 9.837 ou 9,84");

        Assertions.assertTrue(ims > 0);
        Assertions.assertEquals(9.84, ims); // RESULTADO DA PLANILHA
    }

    @Test
    @DisplayName("DRY 2: Deve calcular IMS corretamente")
    void shouldCalculateIMSCorrectlyForDryCow2() {
        AnimalContext ctx = getAnimalDryContext2();
        DryMatterIntakeCalculator intakeCalculator = new DryMatterIntakeCalculator();
        double ims = intakeCalculator.calculatePredictedIntake(ctx);
        System.out.println("IMS: " + ims);
        System.out.println("IMS Planilha: 8.106 ou 8.11");

        Assertions.assertTrue(ims > 0);
        Assertions.assertEquals(8.11, ims); // RESULTADO DA PLANILHA
    }

    private static AnimalContext getAnimalLactatingContext() {
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
//    Gabi - 37
    private static AnimalContext getAnimalDryContext() {
        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.DRY);
        ctx.setBodyWeight(new BigDecimal("548"));
        ctx.setProjectedBodyWeight(new BigDecimal("548"));
        ctx.setWeightChangeGoal(BigDecimal.ZERO);
        ctx.setBalanceDate(LocalDate.of(2025, 8, 21));
        ctx.setLactationNumber(3);
        ctx.setMilkFatPct(new BigDecimal("3.93"));
        ctx.setMilkProteinPct(new BigDecimal("3.32"));
        ctx.setSize(AnimalSize.MEDIUM);
        ctx.setMilkYield(new BigDecimal("0.00"));
        ctx.setMilkYieldForCalculations(new BigDecimal("0.00"));
        ctx.setEcc(new BigDecimal("4.00"));
        ctx.setNextVisitDate(LocalDate.of(2025, 9, 20));
        ctx.setAmbientTemperature(new BigDecimal("35"));
        return ctx;
    }
//    Petunia - 39
    private static AnimalContext getAnimalDryContext2() {
        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.DRY);
        ctx.setBodyWeight(new BigDecimal("651"));
        ctx.setWeightChangeGoal(BigDecimal.ZERO);
        ctx.setProjectedBodyWeight(new BigDecimal("651"));
        ctx.setBalanceDate(LocalDate.of(2025, 8, 21));
        ctx.setLactationNumber(2);
        ctx.setMilkFatPct(new BigDecimal("3.93"));
        ctx.setMilkProteinPct(new BigDecimal("3.32"));
        ctx.setSize(AnimalSize.MEDIUM);
        ctx.setMilkYield(new BigDecimal("0.00"));
        ctx.setMilkYieldForCalculations(new BigDecimal("0.00"));
        ctx.setEcc(new BigDecimal("3.50"));
        ctx.setNextVisitDate(LocalDate.of(2025, 9, 20));
        ctx.setAmbientTemperature(new BigDecimal("35"));
        return ctx;
    }
}