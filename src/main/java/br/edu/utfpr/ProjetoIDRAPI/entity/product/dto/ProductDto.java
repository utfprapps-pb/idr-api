package br.edu.utfpr.ProjetoIDRAPI.entity.product.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.activeprinciple.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.entity.productcategory.ProductCategory;
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
