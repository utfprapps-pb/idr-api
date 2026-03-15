package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculadora para estimar a IMS ingestão de Matéria Seca predita.
 */
@Service
public class DryMatterIntakeCalculator {

    public double calculatePredictedIntake(AnimalContext ctx) {
        double baseIntake;

        // Proteção contra nulos
        if (ctx.getStage() == null) return 0.0;

        // Seleção de Fórmula Base
        // Nota: Assumindo que lactationNumber == 0 define Novilha.
        // Se houver enum específico HEIFER, usar ctx.getStage() == ProductionStage.HEIFER
        if (ctx.getStage() == ProductionStage.HEIFER) {
            baseIntake = calculateHeiferIntake(ctx);
        } else if (ctx.getStage().isProducingMilk()) {
            baseIntake = calculateLactatingCowIntake(ctx);
        } else {
            // Vacas Secas / Pré-Parto
            baseIntake = calculateDryCowIntake(ctx);
        }

        // Aplicação do Estresse Térmico (> 20°C)
        // Extraímos o valor double para comparação
        double currentTemp = ctx.getAmbientTemperature() != null ? ctx.getAmbientTemperature().doubleValue() : 20.0;
        double threshold = NrcConstants.Intake.HEAT_STRESS_THRESHOLD.doubleValue();

        double finalIntake = baseIntake;

        if (currentTemp >= threshold) {
            // Fórmula: IMS * (1 - ((Temp - 20) * 0.005922))
            double correctionFactor = 1.0 - ((currentTemp - threshold) * NrcConstants.Intake.HEAT_STRESS_COEFF.doubleValue());
            finalIntake = baseIntake * correctionFactor;
        }

        return BigDecimal.valueOf(finalIntake)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // --- C21: Novilhas ---
    private double calculateHeiferIntake(AnimalContext ctx) {
        long daysPregnant = ctx.getDaysPregnant();

        // Lógica do Excel: SE((282 - Dias) < 0; -1; -(282 - Dias))
        // Simplificação matemática: Se Dias > 282, usa -1. Senão, usa (Dias - 282).
        double daysTerm = 282.0 - daysPregnant;
        double exponentInput = (daysTerm < 0) ? -1.0 : -daysTerm;

        // Recupera constantes como double
        double intercept = NrcConstants.Intake.HEIFER_INTERCEPT.doubleValue();
        double expCoeff = NrcConstants.Intake.HEIFER_EXP_COEFF.doubleValue();
        double expRate = NrcConstants.Intake.HEIFER_EXP_RATE.doubleValue();

        // Fórmula: (Intercept - (Coeff * exp(Rate * Input))) / 100 * Peso
        double percentBw = intercept - (expCoeff * Math.exp(expRate * exponentInput));

        return (percentBw / 100.0) * ctx.getProjectedBodyWeight().doubleValue();
    }

    // --- C23: Vacas Secas ---
    private double calculateDryCowIntake(AnimalContext ctx) {
        long daysPregnant = ctx.getDaysPregnant();

        // Lógica do Excel corrigida: EXP(0.16 * (SE(Dias > 282; 0; Dias) - 282))
        // O termo dentro do exponencial é: (Dias > 282 ? 0 : Dias) - 282
        double daysRestricted = (daysPregnant > 282) ? 0.0 : (double) daysPregnant;
        double exponentInput = daysRestricted - 282.0;

        double intercept = NrcConstants.Intake.DRY_COW_INTERCEPT.doubleValue();
        double expCoeff = NrcConstants.Intake.DRY_COW_EXP_COEFF.doubleValue();
        double expRate = NrcConstants.Intake.DRY_COW_EXP_RATE.doubleValue();

        double percentBw = intercept - (expCoeff * Math.exp(expRate * exponentInput));

        return (percentBw / 100.0) * ctx.getProjectedBodyWeight().doubleValue();
    }

    // --- C26: Vacas em Lactação ---
    private double calculateLactatingCowIntake(AnimalContext ctx) {
        // Conversões necessárias
        double fcm = ctx.getFatCorrectedMilk().doubleValue(); // Requer C16 implementado no AnimalContext
        double bodyWeight = ctx.getProjectedBodyWeight().doubleValue();
        long dim = ctx.getDaysInMilk();

        // Constantes
        double fcmCoeff = NrcConstants.Intake.LACT_FCM_COEFF.doubleValue();
        double weightCoeff = NrcConstants.Intake.LACT_WEIGHT_COEFF.doubleValue();
        double weightExp = NrcConstants.Intake.LACT_WEIGHT_EXP.doubleValue();

        // Termo 1: (0.372 * FCM + 0.0968 * PV^0.75)
        double metabolicTerm = (fcmCoeff * fcm) + (weightCoeff * Math.pow(bodyWeight, weightExp));

        // Termo 2: Lag (Atraso)
        // Lógica do Excel: SE(DEL <= 30; 60/7; ARRED(DEL/7; 0))
        // Nota: O Excel usa ARRED (Round), não divisão simples.
        double weekOfLactation;
        if (dim <= 30) {
            weekOfLactation = 60.0 / 7.0;
        } else {
            weekOfLactation = Math.round((double) dim / 7.0); // Segue o ARRED do Excel
        }

        double lagRate = NrcConstants.Intake.LAG_EXP_RATE.doubleValue();
        double lagOffset = NrcConstants.Intake.LAG_OFFSET.doubleValue();

        // Fórmula Lag: 1 - exp(-0.192 * (Semana + 3.67))
        double lagFactor = 1.0 - Math.exp(lagRate * (weekOfLactation + lagOffset));

        return metabolicTerm * lagFactor;
    }
}
