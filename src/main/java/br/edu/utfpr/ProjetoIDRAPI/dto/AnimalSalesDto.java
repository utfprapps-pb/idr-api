package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AnimalSalesDto {
	private long id;
	
	private String dateSale;
	
	private String reason;
	
	private BigDecimal value;
	
	private String destination;
	
	private AnimalDto animal;
}
