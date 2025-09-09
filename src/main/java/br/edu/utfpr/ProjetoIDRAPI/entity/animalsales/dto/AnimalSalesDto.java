package br.edu.utfpr.ProjetoIDRAPI.entity.animalsales.dto;

import java.math.BigDecimal;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import br.edu.utfpr.ProjetoIDRAPI.enums.Destination;
import br.edu.utfpr.ProjetoIDRAPI.enums.Reason;
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
