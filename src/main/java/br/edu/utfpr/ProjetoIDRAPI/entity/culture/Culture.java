package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

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