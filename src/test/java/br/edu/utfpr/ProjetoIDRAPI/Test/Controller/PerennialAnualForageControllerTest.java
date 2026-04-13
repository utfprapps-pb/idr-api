package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.PerennialAnualForageRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.dto.PerennialAnualForageDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PerennialAnualForageControllerTest extends CrudControllerTest<PerennialAnualForage, PerennialAnualForageDto, Long> {

    @Autowired
    private PerennialAnualForageRepository perennialAnualForageRepository;
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
    protected Long persistAndReturnId(PerennialAnualForage entity) {
        return perennialAnualForageRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        perennialAnualForageRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected PerennialAnualForage createValidObject() {
        return TestUtils.createValidPerennialAnualForage(savedProperty);
    }

    @Override
    protected PerennialAnualForage createInvalidObject() {
        return new PerennialAnualForage();
    }

    @Override
    protected String getURL() {
        return "/fodders";
    }

    @Override
    protected Class<PerennialAnualForageDto> getDtoClass() {
        return PerennialAnualForageDto.class;
    }
}