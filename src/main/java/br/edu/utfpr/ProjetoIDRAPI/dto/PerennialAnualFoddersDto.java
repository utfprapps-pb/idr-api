package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PerennialAnualFoddersDto {

    private Long id;

    private PropertyDto property;

    private String type;

    private Double price;

    private String fodder;

    private Double area;

    private Double averageCost;

    private Integer utilLife;

    private LocalDate formationDate;

    private String note;
}
