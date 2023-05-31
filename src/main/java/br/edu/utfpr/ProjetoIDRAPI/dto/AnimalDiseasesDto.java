package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class AnimalDiseasesDto {
	private long id;
	
	private String diagnosis;
	
	private String diagnosisDate;
	
	private AnimalDto animal;
}
