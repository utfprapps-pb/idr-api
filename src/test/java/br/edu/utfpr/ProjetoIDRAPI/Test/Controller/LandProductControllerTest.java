package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.LandProductRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.landproduct.dto.LandProductDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LandProductControllerTest extends CrudControllerTest<LandProduct, LandProductDto, Long> {

    @Autowired
    private LandProductRepository landProductRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    private Property savedProperty;

    @BeforeEach
    void setup() {
        User owner = TestUtils.createValidUser("Owner");
        userRepository.save(owner);

        User techUser = TestUtils.createValidUser("Tech");
        userRepository.save(techUser);

        PropertyTechnician technician = TestUtils.createValidPropertyTechnician(techUser);
        savedProperty = TestUtils.createValidProperty(owner, List.of(technician));
        propertyRepository.save(savedProperty);
    }

    @Override
    protected Long persistAndReturnId(LandProduct entity) {
        return landProductRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        landProductRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected LandProduct createValidObject() {
        return TestUtils.createValidLandProduct(savedProperty);
    }

    @Override
    protected LandProduct createInvalidObject() {
        return new LandProduct();
    }

    @Override
    protected String getURL() {
        return "/landProducts";
    }

    @Override
    protected Class<LandProductDto> getDtoClass() {
        return LandProductDto.class;
    }
}