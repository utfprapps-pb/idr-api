package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
public class ForageUpdateDto {

    private String cultivation;
    private String area;
    private String averageCost;
    private String usefulLife;
    private LocalDate formation;
    private String growthCycle;
}
