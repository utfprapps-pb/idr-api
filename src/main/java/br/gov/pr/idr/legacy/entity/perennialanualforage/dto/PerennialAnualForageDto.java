package br.gov.pr.idr.legacy.entity.perennialanualforage.dto;

import br.gov.pr.idr.legacy.entity.property.dto.PropertyDto;
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
