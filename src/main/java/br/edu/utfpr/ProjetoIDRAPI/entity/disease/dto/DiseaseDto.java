package br.edu.utfpr.ProjetoIDRAPI.entity.disease.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import lombok.Data;

@Data
public class DiseaseDto {
	private long id;
	
	private String diseaseName;
	
	public Disease toDisease() {
		Disease dis = new Disease();
		dis.setId(id);
		dis.setDiseaseName(diseaseName);
		
		return dis;
	}
}
