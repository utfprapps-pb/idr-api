package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PerennialAnualForageDto {

    private long id;

    private PropertyDto property;

    private String type;

    private Double price;

    private String forage;

    private Double area;

    private Double averageCost;

    private Integer utilLife;

    private LocalDate formationDate;

    private String note;
}
