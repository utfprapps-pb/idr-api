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

	private Double ms;
	private Double pb;
	private Double pm;
	private Double pdr;
	private Double pndr;
	private Double pidn;
	private Double pida;
	private Double ndt;
	private Double em;
	private Double ell;
	private Double ee;
	private Double fda;
	private Double fdn;
	private Double efdn;
	private Double dfdn;
	private Double ufdn;
	private Double cnf;
	private Double amido;
	private Double cinzas;
	private Double ca;
	private Double p;
	private Double mg;
	private Double na;
	private Double cl;
	private Double k;
	private Double s;
	private Double ppmcu;
	private Double ppmzn;
	private Double ppmfe;
	private Double ppmmn;
	private Double ppmi;
	private Double ppmco;
	private Double ppmse;
	private Double vitaui;
	private Double vitdui;
	private Double viteui;
}