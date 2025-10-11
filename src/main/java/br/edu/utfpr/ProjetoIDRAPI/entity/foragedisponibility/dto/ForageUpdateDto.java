package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ForageUpdateDto {

    private String cultivation;
    private String area;
    private String averageCost;
    private String usefulLife;
    private String formation;
    private String growthCycle;
}
