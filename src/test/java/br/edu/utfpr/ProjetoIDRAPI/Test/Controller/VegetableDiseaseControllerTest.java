package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.DiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.VegetableDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.dto.VegetableDiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VegetableDiseaseControllerTest extends CrudControllerTest<VegetableDisease, VegetableDiseaseDto, Long> {

    @Autowired
    private VegetableDiseaseRepository vegetableDiseaseRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private CultureRepository cultureRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    private Property savedProperty;
    private Disease savedDisease;
    private Culture savedCulture;

    @BeforeEach
    void setup() {
        User owner = TestUtils.createValidUser("Owner");
        userRepository.save(owner);

        User techUser = TestUtils.createValidUser("Tech");
        userRepository.save(techUser);

        PropertyTechnician technician = TestUtils.createValidPropertyTechnician(techUser);
        savedProperty = TestUtils.createValidProperty(owner, List.of(technician));
        propertyRepository.save(savedProperty);

        savedDisease = TestUtils.createValidDisease();
        diseaseRepository.save(savedDisease);

        savedCulture = TestUtils.createValidCulture();
        cultureRepository.save(savedCulture);
    }

    @Override
    protected Long persistAndReturnId(VegetableDisease entity) {
        return vegetableDiseaseRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        vegetableDiseaseRepository.deleteAll();
        diseaseRepository.deleteAll();
        cultureRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected VegetableDisease createValidObject() {
        return TestUtils.createValidVegetableDisease(savedDisease, savedCulture, savedProperty);
    }

    @Override
    protected VegetableDisease createInvalidObject() {
        return new VegetableDisease();
    }

    @Override
    protected String getURL() {
        return "/vegetablediseases";
    }

    @Override
    protected Class<VegetableDiseaseDto> getDtoClass() {
        return VegetableDiseaseDto.class;
    }
}