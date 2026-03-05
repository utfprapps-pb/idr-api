package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants.Energy;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NetEnergyCalculatorService {

    public BigDecimal calculateELMaintenance(AnimalContext context, double distanceKm, int timesPerDay) {
        // PV líquido (C6 - C58)
        double netWeight = context.getProjectedBodyWeight().subtract(context.getConceptusWeight()).doubleValue();

        // Mantença Base: (PV^0.75 * 0.08)
        double baseMaint = Math.pow(netWeight, Energy.MAINTENANCE_EXPONENT.doubleValue())
                * Energy.MAINTENANCE_COEFFICIENT.doubleValue();

        // Gasto Atividade: ((2*Dist) * (0.00045*PV)) + (0.0012*PV)
        double pv = context.getProjectedBodyWeight().doubleValue();
        double activity = ((timesPerDay * 2) * (Energy.ACTIVITY_DIST_FACTOR.doubleValue() * pv))
                + (Energy.ACTIVITY_BW_FACTOR.doubleValue() * pv);

        return BigDecimal.valueOf(baseMaint + activity).setScale(3, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateELLactation(AnimalContext context) {
        if (context.getStage() == ProductionStage.DRY || context.getMilkYield() == null) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        double fat = context.getMilkFatPct().doubleValue();
        double prot = context.getMilkProteinPct().doubleValue();

        double energyPerKg = (Energy.MILK_FAT_COEFFICIENT.doubleValue() * fat)
                + (Energy.MILK_PROT_COEFFICIENT.doubleValue() * prot)
                + Energy.MILK_BASE_ENERGY.doubleValue();

        return BigDecimal.valueOf(energyPerKg * context.getMilkYield().doubleValue()).setScale(3, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateELGestation(AnimalContext context, double birthWeight) {
        long daysPreg = context.getDaysPregnant();

        if (daysPreg < Energy.GEST_MIN_DAYS || daysPreg > Energy.GEST_MAX_DAYS) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        double factor = (Energy.GEST_DAYS_COEFFICIENT.doubleValue() * daysPreg - Energy.GEST_DAYS_INTERCEPT.doubleValue())
                * (birthWeight / 45.0);

        return BigDecimal.valueOf(factor / Energy.GEST_EFFICIENCY.doubleValue()).setScale(3, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateELWeightChange(AnimalContext context, double ecc) {
        if (context.getWeightChangeGoal() == null || context.getWeightChangeGoal().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        // Fator ECC: ((ECC - 1) * 2) + 1
        double eccFactor = ((ecc - 1) * 2) + 1;

        double eFat = (Energy.GAIN_FAT_COEFFICIENT.doubleValue() * eccFactor) * Energy.CALORIC_VALUE_FAT.doubleValue();
        double eProt = (Energy.GAIN_PROT_BASE.doubleValue() - Energy.GAIN_PROT_COEFFICIENT.doubleValue() * eccFactor)
                * Energy.CALORIC_VALUE_PROT.doubleValue();

        double result = (eFat + eProt) * Energy.EFFICIENCY_CONVERSION.doubleValue() * context.getWeightChangeGoal().doubleValue();

        return BigDecimal.valueOf(result).setScale(3, RoundingMode.HALF_UP);
    }
}