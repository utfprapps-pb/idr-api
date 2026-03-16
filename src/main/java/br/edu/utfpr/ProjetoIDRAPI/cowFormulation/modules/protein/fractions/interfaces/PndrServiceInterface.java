package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions.interfaces;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;

import java.math.BigDecimal;

public interface PndrServiceInterface {
    BigDecimal calculatePndrSupply(AnimalContext ctx);
}