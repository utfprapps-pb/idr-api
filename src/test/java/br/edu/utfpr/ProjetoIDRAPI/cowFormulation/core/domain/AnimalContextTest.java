package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

class AnimalContextTest {

    @Test
    @DisplayName("Deve calcular corretamente os Dias em Lactação (DEL)")
    void shouldCalculateDaysInMilk() {
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.LACTATING)
                .balanceDate(LocalDate.of(2026, 1, 30))
                .calvingDate(LocalDate.of(2026, 1, 1)) // 29 dias atrás
                .build();

        Assertions.assertEquals(29, ctx.getDaysInMilk());
    }

    @Test
    @DisplayName("Deve calcular Leite Corrigido (FCM) corretamente")
    void shouldCalculateFatCorrectedMilk() {
        // Cenário: 30kg de leite com 3.5% de gordura
        // Fórmula: (0.4 * 30) + (15 * (3.5/100) * 30) = 12 + 15.75 = 27.75
        AnimalContext ctx = AnimalContext.builder()
                .stage(ProductionStage.LACTATING)
                .milkYield(new BigDecimal("30.0"))
                .milkFatPct(new BigDecimal("3.5"))
                .build();

        BigDecimal fcm = ctx.getFatCorrectedMilk();

        // Usamos compareTo para BigDecimal para ignorar precisão irrelevante (ex: 27.750 vs 27.75)
        Assertions.assertEquals(0, new BigDecimal("27.75").compareTo(fcm));
    }
}