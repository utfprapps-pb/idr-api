package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Culture;
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
