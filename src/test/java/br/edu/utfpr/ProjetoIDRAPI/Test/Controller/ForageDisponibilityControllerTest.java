package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ForageDisponibilityControllerTest extends CrudControllerTest<ForageDisponibility, ForageDisponibilityDto, Long> {

    @Autowired
    private ForageDisponibilityRepository forageDisponibilityRepository;
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
    protected Long persistAndReturnId(ForageDisponibility entity) {
        return forageDisponibilityRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        forageDisponibilityRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected ForageDisponibility createValidObject() {
        return TestUtils.createValidForageDisponibility(savedProperty);
    }

    @Override
    protected ForageDisponibility createInvalidObject() {
        return new ForageDisponibility();
    }

    @Override
    protected String getURL() {
        return "/foragesDisponibilities";
    }

    @Override
    protected Class<ForageDisponibilityDto> getDtoClass() {
        return ForageDisponibilityDto.class;
    }
}