package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;

import java.math.BigDecimal;

public interface RumenUndegradableProteinServiceInterface {
    BigDecimal calculateTotalPUR(AnimalContext ctx);
}

