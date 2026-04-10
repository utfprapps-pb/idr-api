package br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class PropertyCollaboratorDto {

    private Long id;

    private String name;

    private String hoursPerDay;

    private PropertyDto property;
}
