package br.edu.utfpr.ProjetoIDRAPI.entity.product.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.activePrinciple.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.entity.productCategory.ProductCategory;
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
