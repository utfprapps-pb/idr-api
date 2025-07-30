package br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.breed.dto.BreedDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
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
