package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class ForageDisponibilityDto {

	private Long id;
	private String cultivation;       // antes era forage
	private String area;
	private String averageCost;
	private String usefulLife;
	private String formation;         // antes era date
	private String ownershipType;
	private String growthCycle;
	private String observation;       // opcional
	private Float entry;
	private Float residue;
	private Float kg;
	private Float picketArea;
	private Float efficiency;
	private Long numCows;
	private Float kgCows;

	private PropertyDto property;
}
