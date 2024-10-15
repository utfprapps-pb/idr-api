package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.dto.LandProductDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;

public class LandProductControllerTest extends CrudControllerTest<LandProduct, LandProductDto, Long> {

    @Override
    protected LandProduct createValidObject() {
        return LandProduct.builder()
                .usedFor("Test")
                .property(Property.builder().id(1L).build())
                .build();
    }

    @Override
    protected LandProduct createInvalidObject() {
        return LandProduct.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/landProducts";
    }
}