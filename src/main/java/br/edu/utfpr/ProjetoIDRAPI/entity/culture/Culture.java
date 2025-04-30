package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

import br.edu.utfpr.ProjetoIDRAPI.enums.CultureType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Culture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private String cultureName;

	@NotNull
	private CultureType cultureType;

	private Float ms;
	private Float pb;
	private Float pm;
	private Float pdr;
	private Float pndr;
	private Float pidn;
	private Float pida;
	private Float ndt;
	private Float em;
	private Float ell;
	private Float ee;
	private Float fda;
	private Float fdn;
	private Float efdn;
	private Float dfdn;
	private Float ufdn;
	private Float cnf;
	private Float amido;
	private Float cinzas;
	private Float ca;
	private Float p;
	private Float mg;
	private Float na;
	private Float cl;
	private Float k;
	private Float s;
	private Float ppmcu;
	private Float ppmzn;
	private Float ppmfe;
	private Float ppmmn;
	private Float ppmi;
	private Float ppmco;
	private Float ppmse;
	private Float vitaui;
	private Float vitdui;
	private Float viteui;
}