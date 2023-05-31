package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class LandProductDto {

    private long id;

    private PropertyDto property;

    private String useDate;

    private Integer quantity;

    private String usedFor;
}
