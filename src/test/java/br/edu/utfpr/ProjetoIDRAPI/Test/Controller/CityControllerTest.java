package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudDependentControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;

public class CityControllerTest extends CrudDependentControllerTest<Region, City, City, Long> {

    @Override
    protected Region createValidDependencyObject() {
        return Region.builder()
                .name("Teste")
                .build();
    }

    @Override
    protected City createValidObject(Region region) {
        return City.builder()
                .name("Test")
                .cityRegion(region)
                .build();
    }

    @Override
    protected City createValidObject() {
        return null;
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

    @Override
    protected String getDependencyURL() {
        return "/regions";
    }
}