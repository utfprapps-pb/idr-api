package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.RegionRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class CityControllerTest extends CrudControllerTest<City, City, Long> {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private RegionRepository regionRepository;

    private Region savedRegion;

    @BeforeEach
    void setup() {
        savedRegion = TestUtils.createValidRegion();
        regionRepository.save(savedRegion);
    }

    @Override
    protected Long persistAndReturnId(City entity) {
        return cityRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        cityRepository.deleteAll();
        regionRepository.deleteAll();
    }

    @Override
    protected City createValidObject() {
        return TestUtils.createValidCity(savedRegion);
    }

    @Override
    protected City createInvalidObject() {
        return new City();
    }

    @Override
    protected String getURL() {
        return "/cities";
    }

    @Override
    protected Class<City> getDtoClass() {
        return City.class;
    }
}