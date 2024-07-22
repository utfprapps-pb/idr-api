package br.edu.utfpr.ProjetoIDRAPI.entity.culture.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import lombok.Data;

@Data
public class CultureDto {
	private long id;
	
	private String cultureName;
	
	public Culture toCulture() {
		Culture cult = new Culture();
		cult.setId(id);
		cult.setCultureName(cultureName);
		
		return cult;
	}
}
