package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class VegetableDiseaseDto {
	private long id;
	
	private String infestationType;
	
	private String date;
	
	private PropertyDto property;
	
	private CultureDto culture;
	
	private DiseaseDto disease;
}
