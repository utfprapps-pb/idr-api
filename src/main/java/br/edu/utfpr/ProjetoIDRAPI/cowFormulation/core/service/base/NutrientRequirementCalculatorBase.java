package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.service.base;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;

/**
 * Interface genérica para calculadoras de requerimento nutricional.
 * T define o tipo de retorno (pode ser BigDecimal ou um Objeto complexo de resultado).
 */
public interface NutrientRequirementCalculatorBase<T> {
    /**
     * Calcula a exigência nutricional baseada no contexto do animal.
     * @param context O estado atual do animal e ambiente.
     * @return O valor da exigência (ex: Mcal/dia, g/dia).
     */
    T calculateRequirement(AnimalContext context);

    /**
     * Identifica qual nutriente este serviço calcula.
     */
    String getNutrientName();
}
