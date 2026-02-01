package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietIngredient {

    // --- Referência à Entidade Original ---
    private Culture cultureOrigin;

    // --- Identificação ---
    private Long cultureId;
    private String name;

    // --- Inputs (Vêm do Banco + Input do Usuário) ---
    private BigDecimal amountNaturalMatter; // E14: Kg Matéria Natural (Input do Usuário: "Quanto colocar no cocho")

    // --- Dados Nutricionais (Vêm do Banco de Dados - Culture) ---
    private BigDecimal dryMatterPercent;    // Mapeado de Culture.ms
    private BigDecimal peNdfPercent;        // Mapeado de Culture.efdn (FDNe)

    /**
     * CONSTRUTOR INTELIGENTE
     * Recebe a entidade Culture do banco e a quantidade definida pelo usuário.
     */
    @Builder
    public DietIngredient(Culture culture, BigDecimal amountNaturalMatter) {
        this.cultureOrigin = culture;
        this.amountNaturalMatter = amountNaturalMatter;

        if (culture != null) {
            this.cultureId = culture.getId();
            this.name = culture.getCultureName();

            // Conversão Segura: Double (Banco) -> BigDecimal (Cálculo)
            // Tratamento de Nulls: Se o banco estiver vazio, assume ZERO para não quebrar o cálculo

            this.dryMatterPercent = culture.getMs() != null
                    ? BigDecimal.valueOf(culture.getMs())
                    : BigDecimal.ZERO;

            this.peNdfPercent = culture.getEfdn() != null
                    ? BigDecimal.valueOf(culture.getEfdn())
                    : BigDecimal.ZERO;
        } else {
            // Fallback caso venha nulo (evita NullPointerException)
            this.dryMatterPercent = BigDecimal.ZERO;
            this.peNdfPercent = BigDecimal.ZERO;
        }
    }


    /**
     * E63: Ingestão de Matéria Seca do Ingrediente (Kg MS).
     * Fórmula: (Kg MN * %MS) / 100
     */
    public BigDecimal getDryMatterAmount() {
        if (amountNaturalMatter == null || dryMatterPercent == null) return BigDecimal.ZERO;

        return amountNaturalMatter
                .multiply(dryMatterPercent)
                .divide(new BigDecimal("100.0"), MathContext.DECIMAL64);
    }

    /**
     * Kg de FDNe fornecido por este ingrediente.
     * Fórmula: Kg MS * (%FDNe / 100)
     */
    public BigDecimal getPeNdfAmount() {
        if (peNdfPercent == null) return BigDecimal.ZERO;

        return getDryMatterAmount()
                .multiply(peNdfPercent)
                .divide(new BigDecimal("100.0"), MathContext.DECIMAL64);
    }
}