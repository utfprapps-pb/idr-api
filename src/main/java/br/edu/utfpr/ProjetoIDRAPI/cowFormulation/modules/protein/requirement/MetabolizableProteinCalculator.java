package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirement;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.service.base.NutrientRequirementCalculatorBase;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MetabolizableProteinCalculator implements NutrientRequirementCalculatorBase<BigDecimal> {

    private final DryMatterIntakeCalculator intakeCalculator;

//    TODO: Injetar o serviço real de PNDR quando estiver pronto (C111)
//    private final PndrServiceInterface pndrServiceInterface;

    @Override
    public String getNutrientName() {
        return "Proteína Metabolizável (PM)";
    }

    @Override
    public BigDecimal calculateRequirement(AnimalContext ctx) {
        // C98: Se não for lactação, retorna zero (conforme lógica discutida)
        if (!ctx.getStage().isProducingMilk()) {
            return BigDecimal.ZERO;
        }

        // 1. PM Descamação (C91)
        // Fórmula: 0.3 * (PesoVazio ^ 0.6)
        BigDecimal scurfPm = NrcConstants.Protein.SCURF_FACTOR.multiply(
                BigDecimal.valueOf(Math.pow(ctx.getEmptyBodyWeight().doubleValue(), NrcConstants.Protein.SCURF_EXPONENT.doubleValue()))
        );

        // 2. PM Urina (C92)
        // Fórmula: 4.1 * (PesoVazio ^ 0.5)
        BigDecimal urinePm = NrcConstants.Protein.URINE_FACTOR.multiply(
                BigDecimal.valueOf(Math.pow(ctx.getEmptyBodyWeight().doubleValue(), NrcConstants.Protein.URINE_EXPONENT.doubleValue()))
        );

        // 3. PM Fecal (C93)
        // Obtemos o IMS em double e convertemos para BigDecimal para o cálculo preciso
        double imsVal = intakeCalculator.calculatePredictedIntake(ctx);
        BigDecimal imsBigDecimal = BigDecimal.valueOf(imsVal);

        BigDecimal fecalPm = calculateFecalPm(imsBigDecimal, ctx);

        // 4. Soma Total (C97) em gramas
        BigDecimal totalPmGrams = scurfPm.add(urinePm).add(fecalPm);

        // 5. Final (C98) em KG
        // Lógica: ((Total / 0.67) / 1000)
        // MathContext.DECIMAL64 garante precisão durante as divisões
        return totalPmGrams
                .divide(NrcConstants.Protein.PM_TO_MILK_EFFICIENCY, MathContext.DECIMAL64)
                .divide(new BigDecimal("1000.0"), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFecalPm(BigDecimal ims, AnimalContext ctx) {
        // TODO: Exigência de PNDR (C111). Substituir por: pndrCalculator.calculateRequirement(ctx);
        // BigDecimal pndrRequirement = pndrServiceInterface.calculateRequirement(ctx);
        // --- Dependência Externa (Mockada por enquanto) ---
        BigDecimal pndrRequirement = new BigDecimal("0.5");

        // --- Fórmula C93 ---
        // Lógica: (IMS * 30) - 0.5 * (((PNDR / 1.18) * 0.64 / 0.8) - ((PNDR / 1.18) * 0.64))
        // Termo 1: Base Fecal (IMS * 30)
        BigDecimal baseFecal = ims.multiply(NrcConstants.Protein.FECAL_IMS_FACTOR);

        // Termo 2: Cálculos Metabólicos
        // X = (PNDR / 1.18) * 0.64
        BigDecimal termX = pndrRequirement
                .divide(NrcConstants.Protein.PNDR_TO_MCP_EFFICIENCY, MathContext.DECIMAL64) // / 1.18
                .multiply(NrcConstants.Protein.MCP_CONVERSION); // * 0.64

        // Y = (X / 0.8)
        BigDecimal termY = termX.divide(NrcConstants.Protein.RDP_DIGESTIBILITY, MathContext.DECIMAL64); // / 0.8

        // Diferença = Y - X
        BigDecimal metabolicDifference = termY.subtract(termX);

        // Resultado Final: Base - (0.5 * Diferença)
        return baseFecal.subtract(new BigDecimal("0.5").multiply(metabolicDifference));
    }
}