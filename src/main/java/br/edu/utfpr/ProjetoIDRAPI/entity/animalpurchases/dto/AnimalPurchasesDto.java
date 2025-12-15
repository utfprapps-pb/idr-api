package br.edu.utfpr.ProjetoIDRAPI.entity.animalpurchases.dto;

import java.math.BigDecimal;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import lombok.Data;

@Data
public class AnimalPurchasesDto {
	private long id;
	
	private String datePurchase;
	
	private String birthDate;
	
	private BigDecimal value;
	
	private AnimalDto animal;
}
