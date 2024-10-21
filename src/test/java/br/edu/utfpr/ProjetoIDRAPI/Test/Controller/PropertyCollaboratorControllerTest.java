package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator.dto.PropertyCollaboratorDto;

public class PropertyCollaboratorControllerTest extends CrudControllerTest<PropertyCollaborator, PropertyCollaboratorDto, Long> {

    @Override
    protected PropertyCollaborator createValidObject() {
        return PropertyCollaborator.builder()
                .property(Property.builder().id(1L).build())
                .collaboratorName("Test")
                .workHours(8)
                .workDays(5)
                .build();
    }

    @Override
    protected PropertyCollaborator createInvalidObject() {
        return PropertyCollaborator.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/propertyCollaborators";
    }
}