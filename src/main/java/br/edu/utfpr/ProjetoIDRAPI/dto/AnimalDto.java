package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class AnimalDto {

    private long id;

    private PropertyDto property;

    private String type;

    private String identifier;

    private String name;

    private String breed;

    private String bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;
}
