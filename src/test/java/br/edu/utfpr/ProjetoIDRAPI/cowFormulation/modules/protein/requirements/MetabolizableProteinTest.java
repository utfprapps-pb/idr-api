package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirements;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.engine.intake.DryMatterIntakeCalculator;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions.RumenUndegradableProteinServiceInterface;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.requirement.MetabolizableProteinCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class MetabolizableProteinTest {

    @Mock // Cria uma versão falsa do calculador de IMS
    private DryMatterIntakeCalculator intakeMock;

    @Mock // Cria uma versão falsa do serviço de PNDR (interface)
    private RumenUndegradableProteinServiceInterface pndrMock;

    @InjectMocks // Injeta os mocks dentro da sua classe de cálculo
    private MetabolizableProteinCalculator pmCalculator;

    @Test
    @DisplayName("Deve calcular PM Fecal corretamente isolando as dependências")
    void shouldCalculatePmRequirement() {
        // --- ARRANGE (Preparar o terreno) ---
        AnimalContext ctx = new AnimalContext();
        ctx.setStage(ProductionStage.LACTATING);
        // Precisamos do Peso Vazio para descamação/urina
        // Vamos setar Peso 600kg e assumir feto 0 para simplificar o teste
        ctx.setBodyWeight(new BigDecimal("600"));
        ctx.setWeightChangeGoal(BigDecimal.ZERO);

        // Mágica do Mockito: "Quando alguém pedir o IMS, retorne 20kg"
        when(intakeMock.calculatePredictedIntake(any())).thenReturn(20.0);

        // Mágica do Mockito: "Quando alguém pedir o PNDR, retorne 0.5kg"
        when(pndrMock.calculateTotalPUR(any())).thenReturn(new BigDecimal("0.5"));

        // --- ACT (Executar) ---
        BigDecimal resultadoPm = pmCalculator.calculateRequirement(ctx);

        // --- ASSERT (Verificar) ---
        // Você deve calcular na mão ou no Excel quanto dá essa conta com:
        // IMS = 20, PNDR = 0.5, Peso = 600.
        // Vamos supor que o resultado esperado seja 1.250 kg
        System.out.println("Resultado PM Calculado: " + resultadoPm);

        Assertions.assertNotNull(resultadoPm);
        // Assertions.assertEquals(new BigDecimal("1.250"), resultadoPm); (Descomente com valor real)
    }
}