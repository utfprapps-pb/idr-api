package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Plague;
import lombok.Data;

@Data
public class PlagueDto {
	private long id;
	
	private String plagueName;
	
	public Plague toPlague() {
		Plague plag = new Plague();
		plag.setId(id);
		plag.setPlagueName(plagueName);
		
		return plag;
	}
}
