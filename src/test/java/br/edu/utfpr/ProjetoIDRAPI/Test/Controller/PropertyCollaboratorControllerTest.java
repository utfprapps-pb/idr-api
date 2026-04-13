package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaboratorRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.dto.PropertyCollaboratorDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertytechnician.PropertyTechnician;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PropertyCollaboratorControllerTest extends CrudControllerTest<PropertyCollaborator, PropertyCollaboratorDto, Long> {

    @Autowired
    private PropertyCollaboratorRepository propertyCollaboratorRepository;
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
    protected Long persistAndReturnId(PropertyCollaborator entity) {
        return propertyCollaboratorRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        propertyCollaboratorRepository.deleteAll();
        propertyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    protected PropertyCollaborator createValidObject() {
        PropertyCollaborator collaborator = TestUtils.createValidPropertyCollaborator();
        collaborator.setProperty(savedProperty);
        return collaborator;
    }

    @Override
    protected PropertyCollaborator createInvalidObject() {
        return new PropertyCollaborator();
    }

    @Override
    protected String getURL() {
        return "/propertyCollaborators";
    }

    @Override
    protected Class<PropertyCollaboratorDto> getDtoClass() {
        return PropertyCollaboratorDto.class;
    }
}