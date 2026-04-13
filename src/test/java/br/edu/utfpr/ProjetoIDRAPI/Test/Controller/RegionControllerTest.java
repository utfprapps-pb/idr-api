package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class RegionControllerTest extends CrudControllerTest<Region, Region, Long> {

    @Autowired
    private RegionRepository regionRepository;

    @Override
    protected Long persistAndReturnId(Region entity) {
        return regionRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        regionRepository.deleteAll();
    }

    @Override
    protected Region createValidObject() {
        return TestUtils.createValidRegion();
    }

    @Override
    protected Region createInvalidObject() {
        return new Region();
    }

    @Override
    protected String getURL() {
        return "/regions";
    }

    @Override
    protected Class<Region> getDtoClass() {
        return Region.class;
    }
}