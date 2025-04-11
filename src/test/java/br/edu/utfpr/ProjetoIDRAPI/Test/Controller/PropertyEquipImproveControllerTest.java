package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.dto.PropertyEquipImproveDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;

public class PropertyEquipImproveControllerTest extends CrudControllerTest.Dependent<PropertyEquipImprove, PropertyEquipImproveDto, Long> {

    @Override
    protected PropertyEquipImprove createValidObject() {
        return PropertyEquipImprove.builder()
            .name("Teste")
            .type("Teste")
            .property(
                (Property) createValidDependency(
                Property.builder()
                .user(
                    (User) createValidDependency(
                        User.builder()
                        .username("Teste")
                        .displayName("Teste")
                        .password("Teste")
                        .build(),
                        "/users"
                    )
                )
                .leased(false)
                .build(),
                "/properties"
                )
            )
            .build();
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
}