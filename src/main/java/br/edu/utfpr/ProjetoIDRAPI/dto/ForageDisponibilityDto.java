package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.math.BigInteger;
import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import lombok.Data;

@Data
public class ForageDisponibilityDto {
	private Long id;

	private LocalDate date;

	private String forage;

	private Float entry;

	private Float residue;

	private Float kg;

	private Float picketArea;

	private Float efficiency;

	private BigInteger numCows;

	private Float kgCows;

	private Property property;
}
