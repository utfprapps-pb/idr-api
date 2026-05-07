package br.gov.pr.idr.legacy.entity.animaldiseases.dto;

import br.gov.pr.idr.legacy.entity.animal.dto.AnimalDto;
import lombok.Data;

@Data
public class AnimalDiseasesDto {
	private long id;
	
	private String diagnosis;
	
	private String diagnosisDate;
	
	private AnimalDto animal;
}
