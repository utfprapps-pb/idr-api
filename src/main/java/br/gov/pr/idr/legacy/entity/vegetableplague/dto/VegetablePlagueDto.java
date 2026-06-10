package br.gov.pr.idr.legacy.entity.vegetableplague.dto;

import br.gov.pr.idr.legacy.entity.culture.dto.CultureDto;
import br.gov.pr.idr.legacy.entity.plague.dto.PlagueDto;
import br.gov.pr.idr.legacy.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class VegetablePlagueDto {
	private long id;

	private String infestationType;

	private String date;

	private CultureDto culture;

	private PlagueDto plague;
}
