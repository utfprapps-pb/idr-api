package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.PropertyEquipImproveRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.dto.PropertyEquipImproveDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PropertyEquipImproveControllerTest extends CrudControllerTest<PropertyEquipImprove, PropertyEquipImproveDto, Long> {

    @Autowired
    private PropertyEquipImproveRepository propertyEquipImproveRepository;
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
    protected Long persistAndReturnId(PropertyEquipImprove entity) {
        return propertyEquipImproveRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        propertyEquipImproveRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected PropertyEquipImprove createValidObject() {
        return TestUtils.createValidPropertyEquipImprove(savedProperty);
    }

    @Override
    protected PropertyEquipImprove createInvalidObject() {
        return new PropertyEquipImprove();
    }

    @Override
    protected String getURL() {
        return "/propertyEquipImproves";
    }

    @Override
    protected Class<PropertyEquipImproveDto> getDtoClass() {
        return PropertyEquipImproveDto.class;
    }
}