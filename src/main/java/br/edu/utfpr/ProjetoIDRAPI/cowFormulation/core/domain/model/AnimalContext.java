package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.constants.NrcConstants;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.formulation.Formulation;
import br.edu.utfpr.ProjetoIDRAPI.entity.milkControl.MilkControl;
import br.edu.utfpr.ProjetoIDRAPI.entity.reproductiveCycle.ReproductiveCycle;
import br.edu.utfpr.ProjetoIDRAPI.enums.AnimalSize;
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
    private BigDecimal projectedBodyWeight;
    private ProductionStage stage;          // Input manual (não tem no Animal)
    private Integer lactationNumber;        // Inferido de Animal.type ou input manual
    private AnimalSize size;

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

    private BigDecimal ecc;
    private BigDecimal milkYieldForCalculations;

    /**
     * CONSTRUTOR INTELIGENTE (Builder Customizado)
     */
    @Builder
    public AnimalContext(Animal animal,
                         ReproductiveCycle cycle,
                         MilkControl milkControl,
                         Formulation formulation,
                         BigDecimal projectedBodyWeight
    ) {

        this.animalOrigin = animal;
        this.projectedBodyWeight = projectedBodyWeight;

        // --- 1. Extração do Animal (Garantia de Tipagem) ---
        if (animal != null) {
            this.bodyWeight = animal.getCurrentWeight() != null
                    ? BigDecimal.valueOf(animal.getCurrentWeight())
                    : BigDecimal.ZERO;
            this.size = animal.getSize();
            this.ecc = animal.getEcc() != null ? BigDecimal.valueOf(animal.getEcc()) : new BigDecimal("3.0");
        } else {
            this.bodyWeight = BigDecimal.ZERO;
        }

        // --- 2. Extração do Ciclo Reprodutivo ---
        if (cycle != null) {
            this.stage = cycle.getStage();
            this.lactationNumber = cycle.getLactationNumber();
            this.calvingDate = cycle.getCalvingDate();
            this.lastInseminationDate = cycle.getLastInseminationDate();
        } else {
            // Inferência caso não exista ciclo (ex: primeira inserção de Novilha)
            this.stage = ProductionStage.HEIFER;
            this.lactationNumber = (animal != null && "Novilha".equalsIgnoreCase(animal.getType())) ? 0 : 1;
        }

        // --- 3. Extração do Controle Leiteiro ---
        if (milkControl != null) {
            this.milkYield = milkControl.getMilkYield() != null ? BigDecimal.valueOf(milkControl.getMilkYield().doubleValue()) : BigDecimal.ZERO;
            this.milkFatPct = milkControl.getFatPercentage() != null ? BigDecimal.valueOf(milkControl.getFatPercentage().doubleValue()) : BigDecimal.ZERO;
            this.milkProteinPct = milkControl.getProteinPercentage() != null ? BigDecimal.valueOf(milkControl.getProteinPercentage().doubleValue()) : BigDecimal.ZERO;
        } else {
            this.milkYield = BigDecimal.ZERO;
            this.milkFatPct = BigDecimal.ZERO;
            this.milkProteinPct = BigDecimal.ZERO;
        }

        // --- 4. Extração da Formulação / Metas ---
        if (formulation != null) {
            this.balanceDate = formulation.getBalanceDate() != null ? formulation.getBalanceDate() : LocalDate.now();
            this.nextVisitDate = formulation.getNextVisitDate();
            this.weightChangeGoal = formulation.getWeightChangeGoal() != null ? BigDecimal.valueOf(formulation.getWeightChangeGoal().doubleValue()) : BigDecimal.ZERO;
            this.ambientTemperature = formulation.getAmbientTemperature() != null ? BigDecimal.valueOf(formulation.getAmbientTemperature().doubleValue()) : new BigDecimal("20.0");

            // Lógicas de negócio da Formulação (Prioridade do técnico)
            this.milkYieldForCalculations = formulation.getMilkYieldOverride() != null ? BigDecimal.valueOf(formulation.getMilkYieldOverride()) : this.getPeakAdjustedMilkYield();
        } else {
            this.balanceDate = LocalDate.now();
            this.weightChangeGoal = BigDecimal.ZERO;
            this.ambientTemperature = new BigDecimal("20.0"); // 20ºC é o ponto neutro térmico do NRC
            this.ecc = animal != null && animal.getEcc() != null ? BigDecimal.valueOf(animal.getEcc()) : new BigDecimal("3.0");
            this.milkYieldForCalculations = this.getPeakAdjustedMilkYield();
        }
    }

    // ==================================================================================
    // MÉTODOS COMPUTADOS (Regra de Negócio)
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
        // Se temos datas e meta, calculamos a projeção
        if (balanceDate != null && nextVisitDate != null && weightChangeGoal != null) {
            long daysInterval = ChronoUnit.DAYS.between(balanceDate, nextVisitDate);
            if (daysInterval > 0) {
                BigDecimal weightBase = (this.bodyWeight != null) ? this.bodyWeight : this.projectedBodyWeight;
                if (weightBase == null) return BigDecimal.ZERO;

                BigDecimal totalChange = weightChangeGoal.multiply(BigDecimal.valueOf(daysInterval));
                return weightBase.add(totalChange);
            }
        }

        // Fallback: se não puder projetar, tenta usar o peso que estiver disponível
        if (this.projectedBodyWeight != null) return this.projectedBodyWeight;
        if (this.bodyWeight != null) return this.bodyWeight;

        return BigDecimal.ZERO;
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