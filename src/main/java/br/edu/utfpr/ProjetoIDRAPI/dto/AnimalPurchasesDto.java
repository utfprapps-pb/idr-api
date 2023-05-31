package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AnimalPurchasesDto {
	private long id;
	
	private String datePurchase;
	
	private String birthDate;
	
	private BigDecimal value;
	
	private AnimalDto animal;
}
