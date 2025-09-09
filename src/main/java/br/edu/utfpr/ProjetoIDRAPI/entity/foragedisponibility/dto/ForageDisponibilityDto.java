package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import java.math.BigInteger;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class ForageDisponibilityDto {
	private Long id;

	private String date;

	private String forage;

	private Float entry;

	private Float residue;

	private Float kg;

	private Float picketArea;

	private Float efficiency;

	private BigInteger numCows;

	private Float kgCows;

	private PropertyDto property;
}
