package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.feed.dto.FeedDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.dto.PestDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class CultivationPestDto {
	private long id;
	
	private String infestationType;
	
	private String date;
	
	private PropertyDto property;
	
	private FeedDto culture;
	
	private PestDto plague;
}
