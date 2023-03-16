package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class PlagueDiseaseDto {
	private long id;
	
	private VegetableDto vegetable;
	
	private String culture;
	
	private String identification;
	
	private String type;
}
