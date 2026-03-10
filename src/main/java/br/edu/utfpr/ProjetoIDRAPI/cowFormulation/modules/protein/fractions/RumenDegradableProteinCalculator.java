package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculadora da Proteína Degradável no Rúmen (PDR).
 * Representa o cálculo das células C108, C109, C110 e C111 da planilha Exigências NRC 2001 Vacas.
 */

@Service
@RequiredArgsConstructor
public class RumenDegradableProteinCalculator {

    /**
     * C111: Calcula a PDR total (soma de vacas secas, pré-parto e lactação)
     * retorna PDR total (Ex: 22.5 para 22.5%)
    */

    // Calculadora IMS
    private final DryMatterIntakeCalculator intakeCalculator;

    // C111 - Exigencias NRC 2001
    public BigDecimal calculateTotalPDR(AnimalContext ctx){
        // C111: PDR total 
        BigDecimal totalPDR = BigDecimal.ZERO;

        switch(ctx.getStage()){
            case LACTATING:
                return PDRDairyCowsCalculator(ctx);

            case DRY:
                return PDRDryCowsCalculator(ctx);

            case PRE_FRESH:
                return PDRPreCowsCalculator(ctx);

            case HEIFER:
                return /* ???? */ BigDecimal.ZERO;
        }

        if(totalPDR.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
        else if(totalPDR.compareTo(BigDecimal.ZERO) > 0){
            return totalPDR.setScale(3, RoundingMode.HALF_UP);
        }
        else{
            return BigDecimal.ZERO;
        }
    }

    // C108 - Exigencias NRC 2001
    private BigDecimal PDRDryCowsCalculator(AnimalContext ctx){
        if(ctx.getStage() != ProductionStage.DRY)
            return BigDecimal.ZERO;


        BigDecimal projectedBodyWeight = ctx.getProjectedBodyWeight();
        BigDecimal milkFatPct = ctx.getMilkFatPct();
        BigDecimal milkProteinPct = ctx.getMilkProteinPct();
        BigDecimal ims = BigDecimal.valueOf(intakeCalculator.calculatePredictedIntake(ctx));

        BigDecimal pt1 = NrcConstants.Rdp.A_INTERCEPT
                .subtract(NrcConstants.Rdp.A_WEIGHT_COEFF.multiply(projectedBodyWeight))
                .subtract(NrcConstants.Rdp.A_FAT_COEFF.multiply(milkFatPct))
                .add(NrcConstants.Rdp.A_PROTEIN_COEFF_1.multiply(milkProteinPct));

        BigDecimal pt2 = NrcConstants.Rdp.B_INTERCEPT
                .subtract(NrcConstants.Rdp.B_WEIGHT_COEFF.multiply(projectedBodyWeight))
                .add(NrcConstants.Rdp.B_PROTEIN_COEFF_2.multiply(milkProteinPct))
                .subtract(NrcConstants.Rdp.B_FAT_COEFF.multiply(milkFatPct))
                .add(NrcConstants.Rdp.B_PROTEIN_COEFF_3.multiply(milkProteinPct));

        BigDecimal result = NrcConstants.Rdp.BASE_INTERCEPT
                .subtract(NrcConstants.Rdp.FACTOR_A.multiply(pt1))
                .add(NrcConstants.Rdp.FACTOR_B.multiply(pt2));


        return result
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .multiply(ims)
                .setScale(3, RoundingMode.HALF_UP);

    }

    // C109 - Exigencias NRC 2001
    private BigDecimal PDRPreCowsCalculator(AnimalContext ctx) {

        if (ctx.getStage() != ProductionStage.PRE_FRESH)
            return BigDecimal.ZERO;

        BigDecimal projectedBodyWeight = ctx.getProjectedBodyWeight();
        BigDecimal fatContent = ctx.getMilkFatPct();
        BigDecimal proteinContent = ctx.getMilkProteinPct();
        BigDecimal milkProduction = ctx.getPeakAdjustedMilkYield();

        BigDecimal ims = BigDecimal.valueOf(intakeCalculator.calculatePredictedIntake(ctx));

        BigDecimal innerA = NrcConstants.Rdp.A_INTERCEPT
                .subtract(NrcConstants.Rdp.A_WEIGHT_COEFF.multiply(projectedBodyWeight));

        BigDecimal part1 = NrcConstants.Rdp.BASE_INTERCEPT
                .subtract(NrcConstants.Rdp.FACTOR_A.multiply(innerA))
                .add(NrcConstants.Rdp.A_MILK_COEFF.multiply(milkProduction))
                .subtract(NrcConstants.Rdp.A_FAT_COEFF.multiply(fatContent))
                .add(NrcConstants.Rdp.A_PROTEIN_COEFF_1.multiply(proteinContent));

        BigDecimal termB = NrcConstants.Rdp.B_INTERCEPT
                .subtract(NrcConstants.Rdp.B_WEIGHT_COEFF.multiply(projectedBodyWeight))
                .add(NrcConstants.Rdp.B_PROTEIN_COEFF_2.multiply(proteinContent))
                .subtract(NrcConstants.Rdp.B_FAT_COEFF.multiply(fatContent))
                .add(NrcConstants.Rdp.B_PROTEIN_COEFF_3.multiply(proteinContent));

        BigDecimal part2 = NrcConstants.Rdp.FACTOR_B.multiply(termB);

        BigDecimal result = part1
                .add(part2)
                .divide(new BigDecimal("100"))
                .multiply(ims)
                .setScale(3, RoundingMode.HALF_UP);

        return result;

    }

    // C110 - Exigencias NRC 2001
    private BigDecimal PDRDairyCowsCalculator(AnimalContext ctx) {

    if (ctx.getStage() != ProductionStage.LACTATING)
        return BigDecimal.ZERO;


    BigDecimal peso = ctx.getProjectedBodyWeight();
    BigDecimal leite = ctx.getMilkYield();
    BigDecimal gordura = ctx.getMilkFatPct();
    BigDecimal proteina = ctx.getMilkProteinPct();

    // E28 (Consumo) - O Mock do teste deve ser o valor exato da célula E28 da planilha
    BigDecimal ims = BigDecimal.valueOf(intakeCalculator.calculatePredictedIntake(ctx));

    // Executa a fórmula exatamente como escrita no SE() do Excel
    BigDecimal equacao = NrcConstants.Rdp.A_INTERCEPT
            .subtract(NrcConstants.Rdp.A_WEIGHT_COEFF.multiply(peso))
            .add(NrcConstants.Rdp.A_MILK_COEFF.multiply(leite))
            .subtract(NrcConstants.Rdp.A_FAT_COEFF.multiply(gordura))
            .add(NrcConstants.Rdp.A_PROTEIN_COEFF_1.multiply(proteina));

    // (Resultado / 100) * IMS
    return equacao
            .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
            .multiply(ims)
            .setScale(3, RoundingMode.HALF_UP);
    }

}
