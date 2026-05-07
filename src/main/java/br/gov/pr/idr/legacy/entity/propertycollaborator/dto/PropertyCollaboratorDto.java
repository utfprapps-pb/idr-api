package br.gov.pr.idr.legacy.entity.propertycollaborator.dto;

import br.gov.pr.idr.legacy.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class PropertyCollaboratorDto {

    private Long id;

    private String name;

    private String hoursPerDay;

    private PropertyDto property;
}
