package br.gov.pr.idr.legacy.entity.animalpurchases.dto;

import java.math.BigDecimal;

import br.gov.pr.idr.legacy.entity.animal.dto.AnimalDto;
import lombok.Data;

@Data
public class AnimalPurchasesDto {
	private long id;
	
	private String datePurchase;
	
	private String birthDate;
	
	private BigDecimal value;
	
	private AnimalDto animal;
}
