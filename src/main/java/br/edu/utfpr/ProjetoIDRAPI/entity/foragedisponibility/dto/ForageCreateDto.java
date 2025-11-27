package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ForageCreateDto {
    @NotNull
    private Double area;
    @NotNull
    private Double averageCost;
    @NotNull
    private Long usefulLife;
    @NotNull
    private LocalDate formation;
    @NotNull
    private String growthCycle;
    private String observation;
    @NotNull
    private String ownershipType;
    private String cultivation;
    private Double entry;
    private Double residue;
    private Double kg;
    private Double efficiency;
    private Long numCows;
    private Double kgCows;
}
