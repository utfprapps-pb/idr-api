package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MetabolizableEnergyCalculator {

    private final DryMatterIntakeCalculator intakeCalculator;

    public BigDecimal calculateRequirement(AnimalContext ctx, BigDecimal netEnergyExigency) {

        // 1. Obter IMS (C28)
        double ims = intakeCalculator.calculatePredictedIntake(ctx);
        if (ims <= 0) return BigDecimal.ZERO;

        // 2. Converter EL para NDT kg/dia (C63)
        // Fórmula: ((EL / 0.0245) + 0.12) / 100
        BigDecimal ndtKg = netEnergyExigency
                .divide(NrcConstants.Energy.NDT_TO_NEL_SLOPE, MathContext.DECIMAL64)
                .add(NrcConstants.Energy.NDT_TO_NEL_INTERCEPT)
                .divide(NrcConstants.Energy.NDT_TO_PERCENT, MathContext.DECIMAL64);

        // 3. Calcular NDT % (C64)
        // Fórmula: (NDT_kg * 100) / IMS
        BigDecimal ndtPercentage = ndtKg.multiply(NrcConstants.Energy.NDT_TO_PERCENT)
                .divide(BigDecimal.valueOf(ims), MathContext.DECIMAL64);

        // 4. Calcular EM Mcal/kg (Fórmula da Imagem)
        // Fórmula: 1.01 * (0.04409 * NDT%) - 0.45
        BigDecimal de = ndtPercentage.multiply(NrcConstants.Energy.TDN_TO_DE_FACTOR);

        return de.multiply(NrcConstants.Energy.DE_TO_ME_SLOPE)
                .subtract(NrcConstants.Energy.DE_TO_ME_INTERCEPT)
                .setScale(2, RoundingMode.HALF_UP);
    }
}