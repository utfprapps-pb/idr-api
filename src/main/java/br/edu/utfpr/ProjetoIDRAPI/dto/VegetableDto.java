package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class VegetableDto {
	private Long id;
	
	private String name;
	
	private PropertyDto property;
}
