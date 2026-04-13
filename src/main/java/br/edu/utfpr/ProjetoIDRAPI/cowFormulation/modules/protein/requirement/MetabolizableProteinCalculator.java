package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirement;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.service.base.NutrientRequirementCalculatorBase;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions.RumenUndegradableProteinCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetabolizableProteinCalculator implements NutrientRequirementCalculatorBase<BigDecimal> {

    private final DryMatterIntakeCalculator intakeCalculator = new DryMatterIntakeCalculator();

    private static final Logger logger = LoggerFactory.getLogger(MetabolizableProteinCalculator.class);

    private final RumenUndegradableProteinCalculator pndrService = new RumenUndegradableProteinCalculator(intakeCalculator);

    @Override
    public String getNutrientName() {
        return "Proteína Metabolizável (PM)";
    }

    @Override
    public BigDecimal calculateRequirement(AnimalContext ctx) {
        // C98: Se não for lactação, retorna zero (conforme lógica discutida)
//        if (!ctx.getStage().isProducingMilk()) {
//            return BigDecimal.ZERO;
//        }

        // 1. PM Descamação (C91)
        // Fórmula: 0.3 * (PesoVazio ^ 0.6)
        BigDecimal scurfPm = NrcConstants.Protein.SCURF_FACTOR.multiply(
                BigDecimal.valueOf(Math.pow(ctx.getEmptyBodyWeight().doubleValue(), NrcConstants.Protein.SCURF_EXPONENT.doubleValue()))
        );
        logger.info("PM Descamação (C91) = {}", scurfPm);

        // 2. PM Urina (C92)
        // Fórmula: 4.1 * (PesoVazio ^ 0.5)
        BigDecimal urinePm = NrcConstants.Protein.URINE_FACTOR.multiply(
                BigDecimal.valueOf(Math.pow(ctx.getEmptyBodyWeight().doubleValue(), NrcConstants.Protein.URINE_EXPONENT.doubleValue()))
        );
        logger.info("PM Urina (C92) = {}", urinePm);

        // 3. PM Fecal (C93)
        // Obtemos o IMS em double e convertemos para BigDecimal para o cálculo preciso
        double imsVal = intakeCalculator.calculatePredictedIntake(ctx);
        BigDecimal imsBigDecimal = BigDecimal.valueOf(imsVal);
        BigDecimal fecalPm = calculateFecalPm(imsBigDecimal, ctx);
        logger.info("PM Fecal (C93) = {}", fecalPm);

        // 4. PM endogena (C94)
        BigDecimal endogenaPm = calculateEndogenaPm(imsBigDecimal);
        logger.info("PM endogena (C94) = {}", endogenaPm);

        // 5. PM PM Lactation (C95)
        BigDecimal lactationPm = calculateLactationPm(ctx);
        logger.info("PM lactation (C94) = {}", lactationPm);

        // 6. PM Gestation (C96)
        BigDecimal gestationPm = calculateGestationPm(ctx);
        logger.info("PM gestation (C94) = {}", gestationPm);

        // 7. Soma Total (C97) em gramas
        BigDecimal totalPmGrams = scurfPm.add(urinePm).add(fecalPm).add(endogenaPm).add(lactationPm).add(gestationPm);
        // 5. Final (C98) em KG
        // Lógica: ((Total / 0.67) / 1000)
        // MathContext.DECIMAL64 garante precisão durante as divisões
        BigDecimal pm = totalPmGrams
                .divide(NrcConstants.Protein.PM_TO_MILK_EFFICIENCY, MathContext.DECIMAL64)
                .divide(new BigDecimal("1000.0"), 3, RoundingMode.HALF_UP);
        logger.info("PM TOTAL (C97) = {}", totalPmGrams);

        return pm;
    }

    /**
     * Calcula a exigência de PM Fecal (em gramas).
     * Fórmula NRC ARRED(((C28*30)-0,5*(((C111/1,18)*0,64/0,8)-((C111/1,18)*0,64)));3)
     */
    private BigDecimal calculateFecalPm(BigDecimal ims, AnimalContext ctx) {
        // TODO: Exigência de PNDR (C111). Substituir por: pndrCalculator.calculateRequirement(ctx);
        BigDecimal pndrRequirement = pndrService.calculateTotalPUR(ctx);
        logger.info("PNDR: {}", pndrRequirement);

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

    /**
     * Calcula a exigência de PM Endógena (em gramas).
     * Fórmula Excel: ARRED((((11,8 * IMS) * 0,4) / 0,67); 3)
     */
    private BigDecimal calculateEndogenaPm(BigDecimal ims) {
        // Proteção contra IMS nulo ou zero (Se a vaca não come, não há trânsito gastrointestinal)
        if (ims == null || ims.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // Passo 1: Perda Bruta (11,8 * IMS)
        BigDecimal grossLoss = ims.multiply(NrcConstants.Protein.ENDOGENOUS_LOSS_FACTOR);

        // Passo 2: Proteína Verdadeira (Passo 1 * 0,4)
        BigDecimal trueProteinLoss = grossLoss.multiply(NrcConstants.Protein.ENDOGENOUS_TRUE_PROTEIN_FRACTION);

        // Passo 3: Conversão para PM e Arredondamento (Passo 2 / 0,67)
        return trueProteinLoss
                .divide(NrcConstants.Protein.PM_TO_MILK_EFFICIENCY, MathContext.DECIMAL64)
                .setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a exigência de PM para Lactação (em gramas).
     * Fórmula NRC: (((Leite * ((0.93 * Proteina%) / 100)) / 0.67) * 1000)
     */
    private BigDecimal calculateLactationPm(AnimalContext ctx) {
        // Se não está produzindo leite, a exigência é zero.
        if (!ctx.getStage().isProducingMilk() || ctx.getMilkYieldForCalculations() == null) {
            return BigDecimal.ZERO;
        }

        // C15: Produção de Leite (usamos o override do técnico se existir)
        BigDecimal milkYield = ctx.getMilkYieldForCalculations();

        // C11: Teor de Proteína do Leite (%)
        // Proteção essencial: se vier nulo do banco, não podemos deixar quebrar.
        BigDecimal proteinPct = ctx.getMilkProteinPct() != null
                ? ctx.getMilkProteinPct()
                : new BigDecimal("3.0"); // Média padrão conservadora

        // Constantes
        BigDecimal trueProteinFactor = new BigDecimal("0.93"); // Converter PB em Proteína Verdadeira
        BigDecimal efficiency = NrcConstants.Protein.PM_TO_MILK_EFFICIENCY; // 0.67

        // Passo 1: ((0.93 * C11) / 100) -> Porcentagem de Proteína Verdadeira
        BigDecimal trueProteinPct = proteinPct.multiply(trueProteinFactor)
                .divide(new BigDecimal("100"), MathContext.DECIMAL64);

        // Passo 2: C15 * Passo 1 -> Kg de Proteína Verdadeira produzida
        BigDecimal trueProteinYield = milkYield.multiply(trueProteinPct);

        // Passo 3 e 4: (Passo 2 / 0.67) * 1000 -> Exigência em gramas
        return trueProteinYield
                .divide(efficiency, MathContext.DECIMAL64)
                .multiply(new BigDecimal("1000"))
                .setScale(3, RoundingMode.HALF_UP); // ARRED(..., 3) do Excel
    }

    /**
     * Calcula a exigência de PM para a Gestação (em gramas).
     * Fórmula NRC: (((0.69 * DiasGestação) - 69.2) * (PesoBezerro / 45)) / 0.33
     */
    private BigDecimal calculateGestationPm(AnimalContext ctx) {
        long daysPregnant = ctx.getDaysPregnant();

        // Regra de exclusão: Fora da janela de 190 a 310 dias, a exigência é zero.
        if (daysPregnant < NrcConstants.Gestation.CONCEPTUS_THRESHOLD_DAYS ||
                daysPregnant > NrcConstants.Gestation.CONCEPTUS_MAX_DAYS) {
            return BigDecimal.ZERO;
        }

        // Variáveis
        BigDecimal days = BigDecimal.valueOf(daysPregnant);

        // C58: Peso Estimado do Feto/Bezerro.
        // Usamos o método já existente no Contexto, com proteção contra nulos ou zeros.
        BigDecimal calfWeight = ctx.getConceptusWeight();
        if (calfWeight == null || calfWeight.compareTo(BigDecimal.ZERO) == 0) {
            // Fallback seguro para evitar zerar a exigência por erro de preenchimento
            calfWeight = NrcConstants.Gestation.STANDARD_CALF_WEIGHT;
        }

        // Passo 1: Proteína Líquida Base ((0.69 * Dias) - 69.2)
        BigDecimal baseRequirement = days
                .multiply(NrcConstants.Gestation.PROTEIN_GROWTH_SLOPE)
                .subtract(NrcConstants.Gestation.PROTEIN_GROWTH_INTERCEPT);

        // Passo 2: Fator de Ajuste de Peso da Raça (Peso / 45)
        BigDecimal weightAdjustmentFactor = calfWeight
                .divide(NrcConstants.Gestation.STANDARD_CALF_WEIGHT, MathContext.DECIMAL64);

        // Passo 3: Proteína Líquida Ajustada
        BigDecimal adjustedNetProtein = baseRequirement.multiply(weightAdjustmentFactor);

        // Passo 4: Conversão para Proteína Metabolizável (/ 0.33)
        return adjustedNetProtein
                .divide(NrcConstants.Gestation.PM_GESTATION_EFFICIENCY, MathContext.DECIMAL64)
                .setScale(3, RoundingMode.HALF_UP);
    }
}