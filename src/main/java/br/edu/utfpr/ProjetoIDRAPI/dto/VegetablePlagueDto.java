package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetablePlague;
import lombok.Data;

@Data
public class VegetablePlagueDto {
	private long id;
	
	private String infestationType;
	
	private LocalDate date;
	
	private PropertyDto property;
	
	private CultureDto culture;
	
	private PlagueDto plague;
	
	public VegetablePlague toVegetablePlague() {
		VegetablePlague veg = new VegetablePlague();
		veg.setId(id);
		veg.setInfestationType(infestationType);
		veg.setDate(date);
		veg.setIdProperty(property.toProperty());
		veg.setIdCulture(culture.toCulture());
		veg.setIdPlague(plague.toPlague());
		
		return veg;
	}
}
