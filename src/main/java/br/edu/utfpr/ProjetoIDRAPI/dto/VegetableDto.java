package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import lombok.Data;

@Data
public class VegetableDto {
	private long id;
	
	private String name;
	
	private PropertyDto property;
	
	public Vegetable toVegetable() {
		Vegetable veg = new Vegetable();
		veg.setId(id);
		veg.setIdProperty(property.toProperty());
		veg.setName(name);
		
		return veg;
	}
}
