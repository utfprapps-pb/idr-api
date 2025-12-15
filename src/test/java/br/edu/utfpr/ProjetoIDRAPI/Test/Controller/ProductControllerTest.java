package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.Product;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.dto.ProductDto;

public class ProductControllerTest extends CrudControllerTest<Product, ProductDto, Long> {

    @Override
    protected Product createValidObject() {
        return Product.builder()
                .name("Test")
                .description("Test")
                .applicationWay("Test")
                .build();
    }

    @Override
    protected Product createInvalidObject() { return Product.builder().build(); }

    @Override
    protected Long getValidId() { return 1L; }

    @Override
    protected String getURL() { return "/products"; }
}
