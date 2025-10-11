package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForageCreateDto {
    @NotNull
    private Float area;
    @NotNull
    private Float averageCost;
    @NotNull
    private Long usefulLife;
    @NotNull
    private String formation;
    @NotNull
    private String growthCycle;
    private String observation;
    @NotNull
    private String ownershipType;
    private String cultivation;
    private Float entry;
    private Float residue;
    private Float kg;
    private Float efficiency;
    private Long numCows;
    private Float kgCows;
}
