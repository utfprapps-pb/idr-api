package br.edu.utfpr.ProjetoIDRAPI.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ForageDisponibilityDto {
	private long id;

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
