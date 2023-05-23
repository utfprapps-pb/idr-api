package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.model.ProductCategory;
import lombok.Data;

@Data
public class ProductDto {

    private long id;

    private String name;

    private String description;

    private String applicationWay;

    private ActivePrinciple activePrinciple;

    private ProductCategory category;

}
