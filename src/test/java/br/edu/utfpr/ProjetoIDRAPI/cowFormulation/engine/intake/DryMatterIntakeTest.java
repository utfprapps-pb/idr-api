package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class DryMatterIntakeTest {

    private final DryMatterIntakeCalculator calculator = new DryMatterIntakeCalculator();

    @Test
    void shouldCalculateIntakeForLactatingCow() {
        // DADO (Arrange) - Um cenário conhecido do Excel
        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.LACTATING);
        ctx.setBodyWeight(new BigDecimal("600")); // Peso Base
        ctx.setWeightChangeGoal(BigDecimal.ZERO);
        // Mockando valores calculados para isolar a fórmula C26
        // Suponha que no Excel deu 22.5 kg de consumo
        // Para um teste real, você deve preencher: Temperatura, Leite, Gordura, DEL...

        // AÇÃO (Act)
        double result = calculator.calculatePredictedIntake(ctx);

        // VERIFICAÇÃO (Assert)
        // Aceitamos um erro de 0.01 kg (arredondamento)
        // Supondo que o esperado seja 0.0 (pois o ctx está vazio neste exemplo)
        Assertions.assertEquals(0.0, result, 0.01);
    }
}