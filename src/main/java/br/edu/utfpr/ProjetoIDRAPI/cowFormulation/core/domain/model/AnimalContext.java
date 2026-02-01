package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalContext {

    // --- Referência Opcional à Entidade Original ---
    private Animal animalOrigin;

    // --- 1. Dados de Identificação e Estado ---
    private BigDecimal bodyWeight;          // Convertido de Animal.currentWeight
    private ProductionStage stage;          // Input manual (não tem no Animal)
    private Integer lactationNumber;        // Inferido de Animal.type ou input manual

    // --- 2. Dados de Datas (Eventos Dinâmicos) ---
    // Estes dados NÃO estão na entidade Animal, pois variam a cada ciclo reprodutivo
    private LocalDate balanceDate;
    private LocalDate nextVisitDate;
    private LocalDate calvingDate;
    private LocalDate lastInseminationDate;

    // --- 3. Dados Produtivos (Cenário) ---
    private BigDecimal milkYield;
    private BigDecimal milkFatPct;
    private BigDecimal milkProteinPct;
    private BigDecimal weightChangeGoal;

    // --- 4. Dados Ambientais ---
    private BigDecimal ambientTemperature;

    /**
     * CONSTRUTOR INTELIGENTE (Builder Customizado)
     * Este é o segredo da refatoração. Ele pega o Animal do banco e preenche o Contexto.
     */
    @Builder
    public AnimalContext(Animal animal,
                         ProductionStage stage,
                         LocalDate balanceDate,
                         LocalDate nextVisitDate,
                         LocalDate calvingDate,
                         LocalDate lastInseminationDate,
                         BigDecimal milkYield,
                         BigDecimal milkFatPct,
                         BigDecimal milkProteinPct,
                         BigDecimal weightChangeGoal,
                         BigDecimal ambientTemperature) {

        this.animalOrigin = animal;
        this.balanceDate = balanceDate;
        this.nextVisitDate = nextVisitDate;
        this.calvingDate = calvingDate;
        this.lastInseminationDate = lastInseminationDate;
        this.stage = stage;
        this.milkYield = milkYield;
        this.milkFatPct = milkFatPct;
        this.milkProteinPct = milkProteinPct;
        this.weightChangeGoal = weightChangeGoal;
        this.ambientTemperature = ambientTemperature;

        // --- Lógica de Extração da Entidade Animal ---
        if (animal != null) {
            // 1. Conversão Segura de Peso (Float -> BigDecimal)
            this.bodyWeight = animal.getCurrentWeight() != null
                    ? BigDecimal.valueOf(animal.getCurrentWeight())
                    : BigDecimal.ZERO;

            // 2. Inferência de Lactação baseada no Tipo
            // Se for Novilha, Lactação = 0. Caso contrário, assumimos > 0 (ou deve ser passado no futuro)
            if (animal.getType() != null && animal.getType().equalsIgnoreCase("Novilha")) {
                this.lactationNumber = 0;
            } else {
                // Se for Vaca, assumimos 1 por padrão se não tiver histórico,
                // mas idealmente isso viria de um count de partos do banco.
                this.lactationNumber = 1;
            }
        } else {
            // Fallback se não passar animal (Simulação pura)
            this.bodyWeight = BigDecimal.ZERO;
            this.lactationNumber = 1;
        }
    }

    // ==================================================================================
    // MÉTODOS COMPUTADOS (Regra de Negócio Pura)
    // ==================================================================================

    /**
     * C53: Dias em Lactação (DEL).
     * Calcula a diferença entre Data Balanceamento e Parto.
     */
    public long getDaysInMilk() {
        if (calvingDate == null || balanceDate == null || !stage.isProducingMilk()) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(calvingDate, balanceDate);
        return Math.max(0, days);
    }

    /**
     * C57: Dias de Gestação.
     * Calcula a diferença entre Data Balanceamento e Última IA.
     */
    public long getDaysPregnant() {
        if (lastInseminationDate == null || balanceDate == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(lastInseminationDate, balanceDate);
        return Math.max(0, days);
    }

    /**
     * C82: Peso Estimado no Próximo Balanceamento.
     * Peso Atual + (Dias até próxima visita * Taxa de Ganho/Perda).
     */
    public BigDecimal getProjectedBodyWeight() {
        if (balanceDate == null || nextVisitDate == null || weightChangeGoal == null) {
            return this.bodyWeight;
        }
        long daysInterval = ChronoUnit.DAYS.between(balanceDate, nextVisitDate);
        if (daysInterval <= 0) return this.bodyWeight;

        BigDecimal totalChange = weightChangeGoal.multiply(BigDecimal.valueOf(daysInterval));
        return this.bodyWeight.add(totalChange);
    }

    /**
     * C58: Peso do Feto (Conceptus Weight).
     * Essencial para descontar do peso da vaca nos cálculos de manutenção.
     * Lógica: Se gestação > 190 dias, calcula o peso do feto.
     */
    public BigDecimal getConceptusWeight() {
        long daysPreg = getDaysPregnant();
        if (daysPreg < NrcConstants.Gestation.CONCEPTUS_THRESHOLD_DAYS) {
            return BigDecimal.ZERO;
        }
        return getProjectedBodyWeight().multiply(NrcConstants.Gestation.FETAL_GROWTH_RATE);
    }

    /**
     * Peso Vivo Vazio (Empty Body Weight - EBW).
     * Usado nas fórmulas C91 (Descamação) e C92 (Urina).
     * EBW = Peso Vivo - Peso Feto
     */
    public BigDecimal getEmptyBodyWeight() {
        return getProjectedBodyWeight().subtract(getConceptusWeight());
    }

    /**
     * C9: Produção de Leite Ajustada para Pico.
     * Adiciona bônus se estiver no pico da lactação.
     */
    public BigDecimal getPeakAdjustedMilkYield() {
        if (!stage.isProducingMilk() || milkYield == null) {
            return BigDecimal.ZERO;
        }
        long dim = getDaysInMilk();
        if (dim >= NrcConstants.Production.PEAK_MILK_START_DAY &&
                dim <= NrcConstants.Production.PEAK_MILK_END_DAY) {
            return this.milkYield.add(NrcConstants.Production.PEAK_MILK_BONUS);
        }
        return this.milkYield;
    }

    /**
     * C16: Leite Corrigido para 4% de Gordura (FCM - Fat Corrected Milk).
     * Fórmula NRC: (0.4 * kgLeite) + (15 * (Gordura/100) * kgLeite)
     * Essencial para a fórmula de Consumo (C26).
     */
    public BigDecimal getFatCorrectedMilk() {
        if (!stage.isProducingMilk() || milkYield == null || milkYield.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal fat = (milkFatPct != null) ? milkFatPct : new BigDecimal("3.5");

        // Uso de constantes literais para evitar recriação de objetos
        BigDecimal termA = new BigDecimal("0.4").multiply(getPeakAdjustedMilkYield());

        BigDecimal fatDecimal = fat.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal termB = new BigDecimal("15").multiply(fatDecimal).multiply(getPeakAdjustedMilkYield());

        return termA.add(termB);
    }

    /**
     * Peso Metabólico (PV ^ 0.75).
     * Retorna double pois é usado em exponenciais complexas.
     */
    public double getMetabolicWeight() {
        return Math.pow(getProjectedBodyWeight().doubleValue(), 0.75);
    }
}