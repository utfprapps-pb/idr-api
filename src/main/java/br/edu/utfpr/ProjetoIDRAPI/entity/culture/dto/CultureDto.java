package br.edu.utfpr.ProjetoIDRAPI.entity.culture.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.enums.CultureType;
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

	private CultureType cultureType;

	private float ms;
	private float pb;
	private float pm;
	private float pdr;
	private float pndr;
	private float pidn;
	private float pida;
	private float ndt;
	private float em;
	private float ell;
	private float ee;
	private float fda;
	private float fdn;
	private float efdn;
	private float dfdn;
	private float ufdn;
	private float cnf;
	private float amido;
	private float cinzas;
	private float ca;
	private float p;
	private float mg;
	private float na;
	private float cl;
	private float k;
	private float s;
	private float ppmcu;
	private float ppmzn;
	private float ppmfe;
	private float ppmmn;
	private float ppmi;
	private float ppmco;
	private float ppmse;
	private float vitaui;
	private float vitdui;
	private float viteui;
}
