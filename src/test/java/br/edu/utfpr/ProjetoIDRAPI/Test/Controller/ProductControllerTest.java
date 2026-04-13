package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.Product;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.ProductRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.dto.ProductDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductControllerTest extends CrudControllerTest<Product, ProductDto, Long> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    protected Long persistAndReturnId(Product entity) {
        return productRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        productRepository.deleteAll();
    }

    @Override
    protected Product createValidObject() {
        return TestUtils.createValidProduct();
    }

    @Override
    protected Product createInvalidObject() {
        return new Product();
    }

    @Override
    protected String getURL() {
        return "/products";
    }

    @Override
    protected Class<ProductDto> getDtoClass() {
        return ProductDto.class;
    }
}