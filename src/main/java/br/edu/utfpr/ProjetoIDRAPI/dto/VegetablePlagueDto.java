package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class VegetablePlagueDto {
	private long id;
	
	private String infestationType;
	
	private String date;
	
	private PropertyDto property;
	
	private CultureDto culture;
	
	private PlagueDto plague;
}
