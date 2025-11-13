package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ForageDisponibilityDto {

	private Long id;
	private String cultivation;       // antes era forage
	private String area;
	private String averageCost;
	private String usefulLife;
	private LocalDate formation;         // antes era date
	private String ownershipType;
	private String growthCycle;
	private String observation;       // opcional
	private Double entry;
	private Double residue;
	private Double kg;
	private Double picketArea;
	private Double efficiency;
	private Long numCows;
	private Double kgCows;

	private PropertyDto property;


}
