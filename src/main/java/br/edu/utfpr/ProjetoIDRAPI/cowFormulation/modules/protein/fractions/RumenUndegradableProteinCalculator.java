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
 * Calculadora da Proteína Não Degradável no Rúmen (PNDR).
 * Representa o cálculo das células C114, C115, C116 e C117 da planilha Exigências NRC 2001 Vacas.
 */

@Service
@RequiredArgsConstructor
public class RumenUndegradableProteinCalculator{

    /**
     * C117: Calcula a PNDR total (soma de vacas secas, pré-parto e lactação)
     * retorna PNDR total (Ex: 22.5 para 22.5%)
    */

    // Calculadora IMS
    private final DryMatterIntakeCalculator intakeCalculator;

    // C117 - Exigencias NRC 2001
    public BigDecimal calculateTotalPUR(AnimalContext ctx){
        // C117: PNDR total 
        BigDecimal totalPUR = BigDecimal.ZERO;

        switch(ctx.getStage()){
            case LACTATING:
                return PURDairyCowsCalculator(ctx);

            case DRY:
                return purDryCowsCalculator(ctx);

            case PRE_FRESH:
                return PURPreCowsCalculator(ctx);

            case HEIFER:
                return /* ???? */ BigDecimal.ZERO;
        }

        if(totalPUR.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
        else if(totalPUR.compareTo(BigDecimal.ZERO) > 0){
            return totalPUR.setScale(3, RoundingMode.HALF_UP);
        }
        else{
            return BigDecimal.ZERO;
        }
    }

    // C114 - Exigencias NRC 2001
    private BigDecimal purDryCowsCalculator(AnimalContext ctx){

        BigDecimal projectedBodyWeight = ctx.getProjectedBodyWeight();
        BigDecimal fatContent = ctx.getMilkFatPct();
        BigDecimal proteinContent = ctx.getMilkProteinPct();

        BigDecimal ims = BigDecimal.valueOf(intakeCalculator.calculatePredictedIntake(ctx));

        BigDecimal result = NrcConstants.Rdp.A_INTERCEPT
                .subtract(NrcConstants.Rdp.A_WEIGHT_COEFF.multiply(projectedBodyWeight))
                .subtract(NrcConstants.Rdp.A_FAT_COEFF.multiply(fatContent))
                .add(NrcConstants.Rdp.A_PROTEIN_COEFF_1.multiply(proteinContent))
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .multiply(ims)
                .setScale(3, RoundingMode.HALF_UP);

        return result;
    }

    // C115 - Exigencias NRC 2001
    private BigDecimal PURPreCowsCalculator(AnimalContext ctx) {
        if(ctx.getStage() != ProductionStage.PRE_FRESH){
            return BigDecimal.ZERO;
        }

        BigDecimal projectedBodyWeight = ctx.getProjectedBodyWeight();
        BigDecimal fatContent = ctx.getMilkFatPct();
        BigDecimal proteinContent = ctx.getMilkProteinPct();

        BigDecimal ims = BigDecimal.valueOf(intakeCalculator.calculatePredictedIntake(ctx));

        BigDecimal result = NrcConstants.Rdp.A_INTERCEPT
                .subtract(NrcConstants.Rdp.A_WEIGHT_COEFF.multiply(projectedBodyWeight))
                .subtract(NrcConstants.Rdp.A_FAT_COEFF.multiply(fatContent))
                .add(NrcConstants.Rdp.A_PROTEIN_COEFF_1.multiply(proteinContent))
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .multiply(ims)
                .setScale(3, RoundingMode.HALF_UP);

        return result;
    }

    // C116 - Exigencias NRC 2001
    private BigDecimal PURDairyCowsCalculator(AnimalContext ctx){
        if(ctx.getStage() != ProductionStage.LACTATING){
            return BigDecimal.ZERO;
        }

        BigDecimal projectedBodyWeight = ctx.getProjectedBodyWeight();
        BigDecimal fatContent = ctx.getMilkFatPct();
        BigDecimal proteinContent = ctx.getMilkProteinPct();
        BigDecimal milkProduction = ctx.getPeakAdjustedMilkYield();

        BigDecimal ims = BigDecimal.valueOf(intakeCalculator.calculatePredictedIntake(ctx));

        BigDecimal result = NrcConstants.Rdp.A_INTERCEPT
                .subtract(NrcConstants.Rdp.A_WEIGHT_COEFF.multiply(projectedBodyWeight))
                .add(NrcConstants.Rdp.A_MILK_COEFF.multiply(milkProduction))
                .subtract(NrcConstants.Rdp.A_FAT_COEFF.multiply(fatContent))
                .add(NrcConstants.Rdp.A_PROTEIN_COEFF_1.multiply(proteinContent))
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .multiply(ims)
                .setScale(3, RoundingMode.HALF_UP);

        return result;
    
    }
}