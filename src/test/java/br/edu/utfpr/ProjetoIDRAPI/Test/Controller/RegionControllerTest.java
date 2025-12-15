package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;

public class RegionControllerTest extends CrudControllerTest<Region, Region, Long> {

    @Override
    protected Region createValidObject() {
        return Region.builder()
                .name("Teste")
                .build();
    }

    @Override
    protected Region createInvalidObject() {
        return Region.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/regions";
    }
}