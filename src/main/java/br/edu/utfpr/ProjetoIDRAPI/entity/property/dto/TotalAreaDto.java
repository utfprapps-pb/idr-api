package br.edu.utfpr.ProjetoIDRAPI.entity.property.dto;

import java.math.BigDecimal;

public record TotalAreaDto(
        BigDecimal dairyCattleFarming,
        BigDecimal perennialPasture,
        BigDecimal summerPlowing,
        BigDecimal winterPlowing
) {
}
