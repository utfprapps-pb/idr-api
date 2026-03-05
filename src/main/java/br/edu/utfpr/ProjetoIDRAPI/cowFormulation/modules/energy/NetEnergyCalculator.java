package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy.NetEnergyCalculatorService;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class NetEnergyCalculator {

    private final NetEnergyCalculatorService energyService;
    private final DryMatterIntakeCalculator intakeCalculator;

    /**
     * Soma todas as Energias Líquidas (Mcal/dia)
     */
    public BigDecimal calculateTotalEL(AnimalContext context,
                                       double distanceKm,
                                       int timesPerDay,
                                       double birthWeight,
                                       double ecc) {

        BigDecimal elMaintenance = energyService
                .calculateELMaintenance(context, distanceKm, timesPerDay);

        BigDecimal elLactation = energyService
                .calculateELLactation(context);

        BigDecimal elGestation = energyService
                .calculateELGestation(context, birthWeight);

        BigDecimal elWeightChange = energyService
                .calculateELWeightChange(context, ecc);

        return elMaintenance
                .add(elLactation)
                .add(elGestation)
                .add(elWeightChange)
                .setScale(3, RoundingMode.HALF_UP);
    }

    /**
     * NE = ELtotal / IMS
     */
    public BigDecimal calculateNetEnergyPerKg(AnimalContext context,
                                              double distanceKm,
                                              int timesPerDay,
                                              double birthWeight,
                                              double ecc) {

        BigDecimal totalEL = calculateTotalEL(context,
                distanceKm,
                timesPerDay,
                birthWeight,
                ecc);

        double ims = intakeCalculator.calculatePredictedIntake(context);

        if (ims <= 0) {
            return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP);
        }

        return totalEL
                .divide(BigDecimal.valueOf(ims), MathContext.DECIMAL64)
                .setScale(3, RoundingMode.HALF_UP);
    }
}