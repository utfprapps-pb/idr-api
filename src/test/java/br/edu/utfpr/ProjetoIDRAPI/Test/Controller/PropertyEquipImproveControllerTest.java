package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudDependentControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.dto.PropertyEquipImproveDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;

public class PropertyEquipImproveControllerTest extends CrudDependentControllerTest<Property, PropertyEquipImprove, PropertyEquipImproveDto, Long> {

    @Override
    protected Property createValidDependencyObject() {
        return Property.builder()
                .user(User.builder().id(1L).build())
                .leased(false)
                .build();
    }

    @Override
    protected PropertyEquipImprove createValidObject(Property property) {
        return PropertyEquipImprove.builder()
                .name("Teste")
                .type("Teste")
                .property(property)
                .build();
    }

    @Override
    protected PropertyEquipImprove createValidObject() {
        return null;
    }

    @Override
    protected PropertyEquipImprove createInvalidObject() {
        return PropertyEquipImprove.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/propertyEquipImproves";
    }

    @Override
    protected String getDependencyURL() {
        return "/properties";
    }

}