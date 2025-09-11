package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class ForageDisponibilityDto {

	private Long id;
	private String cultivation;       // antes era forage
	private String area;              // caso precise, converta de picketArea ou outra propriedade
	private String averageCost;       // mapear se tiver campo equivalente no backend
	private String usefulLife;        // mapear se tiver campo equivalente
	private String formation;         // antes era date (converta para string YYYY-MM-DD)
	private String ownershipType;     // antes era propriedade do tipo PropertyDto ou enum
	private String growthCycle;       // antes era propriedade do tipo enum
	private String observation;       // opcional, mapear se tiver campo
	private Float entry;
	private Float residue;
	private Float kg;
	private Float picketArea;
	private Float efficiency;
	private Long numCows;             // converter BigInteger para Long
	private Float kgCows;

	private PropertyDto property;     // se quiser manter o PropertyDto, ok
}
