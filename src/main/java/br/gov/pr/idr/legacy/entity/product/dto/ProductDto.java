package br.gov.pr.idr.legacy.entity.product.dto;

import br.gov.pr.idr.legacy.entity.activeprinciple.ActivePrinciple;
import br.gov.pr.idr.legacy.entity.productcategory.ProductCategory;
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
