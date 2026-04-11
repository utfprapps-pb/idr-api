package br.edu.utfpr.ProjetoIDRAPI.entity.feed.dto;

import br.edu.utfpr.ProjetoIDRAPI.enums.FeedType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeedDto {
	private long id;

	private String name;

	private FeedType type;

	private BigDecimal ms;
	private BigDecimal pb;
	private BigDecimal pm;
	private BigDecimal pdr;
	private BigDecimal pndr;
	private BigDecimal pidn;
	private BigDecimal pida;
	private BigDecimal ndt;
	private BigDecimal em;
	private BigDecimal ell;
	private BigDecimal ee;
	private BigDecimal fda;
	private BigDecimal fdn;
	private BigDecimal efdn;
	private BigDecimal dfdn;
	private BigDecimal ufdn;
	private BigDecimal cnf;
	private BigDecimal amido;
	private BigDecimal cinzas;
	private BigDecimal ca;
	private BigDecimal p;
	private BigDecimal mg;
	private BigDecimal na;
	private BigDecimal cl;
	private BigDecimal k;
	private BigDecimal s;
	private BigDecimal ppmcu;
	private BigDecimal ppmzn;
	private BigDecimal ppmfe;
	private BigDecimal ppmmn;
	private BigDecimal ppmi;
	private BigDecimal ppmco;
	private BigDecimal ppmse;
	private BigDecimal vitaui;
	private BigDecimal vitdui;
	private BigDecimal viteui;
	private BigDecimal restrictions;
}
