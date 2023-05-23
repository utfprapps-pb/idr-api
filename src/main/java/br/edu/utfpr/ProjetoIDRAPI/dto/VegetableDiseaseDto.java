package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;
import lombok.Data;

@Data
public class VegetableDiseaseDto {
	private long id;
	
	private String infestationType;
	
	private LocalDate date;
	
	private PropertyDto property;
	
	private CultureDto culture;
	
	private DiseaseDto disease;
	
	public VegetableDisease toVegetableDisease() {
		VegetableDisease veg = new VegetableDisease();
		veg.setId(id);
		veg.setInfestationType(infestationType);
		veg.setDate(date);
		veg.setIdProperty(property.toProperty());
		veg.setIdCulture(culture.toCulture());
		veg.setIdDisease(disease.toDisease());
		
		return veg;
	}
}
