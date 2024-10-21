package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;

public class CityControllerTest extends CrudControllerTest<City, City, Long> {

    @Override
    protected City createValidObject() {
        return City.builder()
                .name("Test")
                .cityRegion(Region.builder().id(1L).build())
                .build();
    }

    @Override
    protected City createInvalidObject() {
        return City.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/cities";
    }
}