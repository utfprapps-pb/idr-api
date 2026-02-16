package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.energy;

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
public class MetabolizableEnergyRequirementCalculator {

    /**
     * Fórmula NRC 2001:
     *
     * DE = 0.04409 × NDT
     * ME = 1.01 × DE − 0.45
     *
     * Invertendo para obter NDT a partir de ME:
     *
     * DE = (ME + 0.45) / 1.01
     * NDT = DE / 0.04409
     */
    public BigDecimal calculateNdtRequirement(BigDecimal metabolizableEnergy) {

        if (metabolizableEnergy == null ||
                metabolizableEnergy.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // DE = (ME + intercept) / slope
        BigDecimal digestibleEnergy = metabolizableEnergy
                .add(NrcConstants.Energy.DE_TO_ME_INTERCEPT)
                .divide(NrcConstants.Energy.DE_TO_ME_SLOPE, MathContext.DECIMAL64);

        // NDT = DE / 0.04409
        return digestibleEnergy
                .divide(NrcConstants.Energy.TDN_TO_DE_FACTOR, MathContext.DECIMAL64)
                .setScale(3, RoundingMode.HALF_UP);
    }
}
