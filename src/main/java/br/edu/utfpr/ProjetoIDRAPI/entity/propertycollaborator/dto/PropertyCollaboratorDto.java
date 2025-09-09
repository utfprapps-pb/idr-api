package br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class PropertyCollaboratorDto {

    private long id;

    private String collaboratorName;

    private Integer workHours;

    private Integer workDays;
    
    private PropertyDto property;
}
