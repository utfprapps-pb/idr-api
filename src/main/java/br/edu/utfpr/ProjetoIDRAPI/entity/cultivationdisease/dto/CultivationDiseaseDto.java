package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.feed.dto.FeedDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.dto.DiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class CultivationDiseaseDto {
	private long id;
	
	private String infestationType;
	
	private String date;
	
	private PropertyDto property;
	
	private FeedDto feed;
	
	private DiseaseDto disease;
}
