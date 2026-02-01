package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.fiber;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.dto.DietIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Calculadora para determinar a porcentagem de FDNe na Matéria Seca total da dieta.
 */
@Service
@RequiredArgsConstructor
public class EffectiveFiberCalculator {

    /**
     * Calcula a porcentagem de FDNe na Matéria Seca total da dieta.
     * Representa a célula E539.
     * * @param ingredients Lista de alimentos com suas quantidades.
     * @return Porcentagem de FDNe (Ex: 22.5 para 22.5%)
     */
    public BigDecimal calculateDietPeNdfPercentage(List<DietIngredient> ingredients) {
        // E86: Total Oferecido (Kg MS/dia)
        BigDecimal totalDryMatter = BigDecimal.ZERO;

        // Numerador: Soma de todos os ingredientes FDNe (em Kg)
        BigDecimal totalPeNdfKg = BigDecimal.ZERO;

        for (DietIngredient item : ingredients) {
            totalDryMatter = totalDryMatter.add(item.getDryMatterAmount());
            totalPeNdfKg = totalPeNdfKg.add(item.getPeNdfAmount());
        }

        // Validação da fórmula: SE(OU(E4=""; SOMA(...) = 0); 0; ...)
        if (totalDryMatter.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // Cálculo Final: (Total Kg FDNe * 100) / Total Kg MS
        // ARRED(..., 1)
        return totalPeNdfKg
                .multiply(new BigDecimal("100.0"))
                .divide(totalDryMatter, MathContext.DECIMAL64)
                .setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * Auxiliar: Retorna o Total de MS da Dieta (E86).
     * Útil para validar se a ingestão ofertada bate com a ingestão predita (C28).
     */
    public BigDecimal calculateTotalDryMatter(List<DietIngredient> ingredients) {
        return ingredients.stream()
                .map(DietIngredient::getDryMatterAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(3, RoundingMode.HALF_UP);
    }
}