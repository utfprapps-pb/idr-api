package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions;

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
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PndrCalculatorTest {

    @Mock
    private DryMatterIntakeCalculator intakeCalculator;

    @InjectMocks
    private RumenUndegradableProteinCalculator pndrCalculator;

    @Test
    @DisplayName("Deve calcular PNDR Total (Vaca Leiteira)")
    void shouldCalculateTotalPndr() {
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.LACTATING)
                .projectedBodyWeight(new BigDecimal("453"))
                .bodyWeight(new BigDecimal("453"))
                .weightChangeGoal(new BigDecimal(0.0))
                .milkFatPct(new BigDecimal("3.94"))
                .milkProteinPct(new BigDecimal("3.32"))
                .milkYield(new BigDecimal("26"))
                .balanceDate(LocalDate.of(2025, 8, 29))
                .calvingDate(LocalDate.of(2025, 8, 29))
                .build();

        double ims1Tabela = 17.35;
        when(intakeCalculator.calculatePredictedIntake(any())).thenReturn(ims1Tabela);

        BigDecimal pndr = pndrCalculator.calculateTotalPUR(ctx);

        System.out.println("===============================");
        System.out.println("DADOS DO TESTE (VACA Leiteira):");
        System.out.println("Peso: " + ctx.getProjectedBodyWeight());
        System.out.println("Leite: " + ctx.getMilkYield());
        System.out.println("IMS: " + ims1Tabela);
        System.out.println("-------------------------------");
        System.out.println("PNDR Calculado: " + pndr);
        System.out.println("===============================");

        Assertions.assertNotNull(pndr);

        Assertions.assertTrue(pndr.compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    @DisplayName("Deve calcular PDR Total (Vaca Seca)")
    void shouldCalculateDryPdr() {
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.DRY)
                .projectedBodyWeight(new BigDecimal("453"))
                .bodyWeight(new BigDecimal("453"))
                .weightChangeGoal(new BigDecimal(0.0))
                .milkFatPct(new BigDecimal("3.93"))
                .milkProteinPct(new BigDecimal("3.32"))
                .milkYield(new BigDecimal("0"))
                .balanceDate(LocalDate.of(2025, 8, 29))
                .calvingDate(LocalDate.of(2025, 5, 14))
                .build();

        double imsTabela = 8.11;
        when(intakeCalculator.calculatePredictedIntake(any())).thenReturn(imsTabela);

        BigDecimal pndr = pndrCalculator.calculateTotalPUR(ctx);

        System.out.println("===============================");
        System.out.println("DADOS DO TESTE (VACA Seca):");
        System.out.println("Peso: " + ctx.getProjectedBodyWeight());
        System.out.println("Leite: " + ctx.getMilkYield());
        System.out.println("IMS: " + imsTabela);
        System.out.println("-------------------------------");
        System.out.println("PNDR Calculado: " + pndr);
        System.out.println("===============================");

        Assertions.assertNotNull(pndr);

        Assertions.assertTrue(pndr.compareTo(BigDecimal.ZERO) > 0);
    }

    /* TODO: Descomentar e inserir os valores de uma vaca em Pré-Parto
    @Test
    @DisplayName("Deve calcular PDR Total (Vaca Pré-Parto)")
    void shouldCalculatePrePdr(){
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.PRE_FRESH)
                .projectedBodyWeight(new BigDecimal("453"))
                .bodyWeight(new BigDecimal("453"))
                .weightChangeGoal(new BigDecimal(0.0))
                .milkFatPct(new BigDecimal("3.93"))
                .milkProteinPct(new BigDecimal("3.32"))
                .milkYield(new BigDecimal("0"))
                .balanceDate(LocalDate.of(2025, 8, 29))
                .calvingDate(LocalDate.of(2025, 5, 14))
                .build();

        double ims2Tabela = 8.11;
        when(intakeCalculator.calculatePredictedIntake(any())).thenReturn(ims2Tabela);

        BigDecimal pdr1 = pdrCalculator.calculateTotalPDR(ctx);

        System.out.println("===============================");
        System.out.println("DADOS DO TESTE (VACA Seca):");
        System.out.println("Peso: " + ctx.getProjectedBodyWeight());
        System.out.println("Leite: " + ctx.getMilkYield());
        System.out.println("IMS: " + ims2Tabela);
        System.out.println("-------------------------------");
        System.out.println("PDR Calculado: " + pdr1);
        System.out.println("===============================");

        Assertions.assertNotNull(pdr1);

        Assertions.assertTrue(pdr1.compareTo(BigDecimal.ZERO) > 0);
    }
    */

    @Test
    @DisplayName("Deve retornar Zero se não houver consumo (IMS=0)")
    void shouldReturnZeroIfIntakeIsZero() {
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.LACTATING)
                .projectedBodyWeight(new BigDecimal("453"))
                .bodyWeight(new BigDecimal("453"))
                .weightChangeGoal(BigDecimal.ZERO)
                .milkFatPct(new BigDecimal("3.94"))
                .milkProteinPct(new BigDecimal("3.32"))
                .milkYield(new BigDecimal("26"))
                .balanceDate(LocalDate.of(2025, 8, 29))
                .calvingDate(LocalDate.of(2025, 3, 20))
                .build();

        when(intakeCalculator.calculatePredictedIntake(any())).thenReturn(0.0);

        BigDecimal result = pndrCalculator.calculateTotalPUR(ctx);

        Assertions.assertEquals(0, BigDecimal.ZERO.compareTo(result));

        System.out.println("Resultado: " + result);

    }
}