package br.gov.pr.idr.legacy.entity.animalsales.dto;

import java.math.BigDecimal;

import br.gov.pr.idr.legacy.entity.animal.dto.AnimalDto;
import br.gov.pr.idr.legacy.enums.Destination;
import br.gov.pr.idr.legacy.enums.Reason;
import lombok.Data;

@Data
public class AnimalSalesDto {
	private long id;
	
	private String dateSale;
	
	private Reason reason;
	
	private BigDecimal value;
	
	private Destination destination;
	
	private AnimalDto animal;
}
