package br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.dto.CultureDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.dto.DiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
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
