package br.gov.pr.idr.legacy.entity.animal.dto;

import java.time.LocalDate;
import java.util.UUID;
import br.gov.pr.idr.legacy.entity.breed.dto.BreedDto;
import lombok.Data;

@Data
public class AnimalDto {

    private UUID id;

    private UUID propertyId;

    private String type;

    // identifier é o nome do animal
    private String identifier;

    private BreedDto breed;

    private LocalDate bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;
}
