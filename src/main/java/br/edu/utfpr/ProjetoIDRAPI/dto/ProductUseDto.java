package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductUseDto {

    private Long id;

    private Property property;

    private LocalDate useDate;

    private Integer quantity;

    private String usedFor;
}
