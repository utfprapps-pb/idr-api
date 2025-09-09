package br.edu.utfpr.ProjetoIDRAPI.entity.animaldiseases.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import lombok.Data;

@Data
public class AnimalDiseasesDto {
	private long id;
	
	private String diagnosis;
	
	private String diagnosisDate;
	
	private AnimalDto animal;
}
