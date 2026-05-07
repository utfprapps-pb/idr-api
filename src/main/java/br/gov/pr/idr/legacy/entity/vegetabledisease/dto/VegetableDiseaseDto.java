package br.gov.pr.idr.legacy.entity.vegetabledisease.dto;

import br.gov.pr.idr.legacy.entity.culture.dto.CultureDto;
import br.gov.pr.idr.legacy.entity.disease.dto.DiseaseDto;
import br.gov.pr.idr.legacy.entity.property.dto.PropertyDto;
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
