package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class PropertyCollaboratorDto {

    private long id;

    private String collaboratorName;

    private Integer workHours;

    private Integer workDays;
}
