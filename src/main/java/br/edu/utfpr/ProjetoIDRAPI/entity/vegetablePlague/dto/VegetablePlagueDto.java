package br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.dto.CultureDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.dto.PlagueDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class VegetablePlagueDto {
	private long id;
	
	private String infestationType;
	
	private String date;
	
	private PropertyDto property;
	
	private CultureDto culture;
	
	private PlagueDto plague;
}
