package br.gov.pr.idr.legacy.entity.animal.dto;

import br.gov.pr.idr.legacy.entity.breed.dto.BreedDto;
import br.gov.pr.idr.legacy.entity.property.dto.PropertyDto;
import lombok.Data;

@Data
public class AnimalDto {

    private long id;

    private PropertyDto property;

    private String type;

    // identifier é o nome do animal
    private String identifier;

    private BreedDto breed;

    private String bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;
}
