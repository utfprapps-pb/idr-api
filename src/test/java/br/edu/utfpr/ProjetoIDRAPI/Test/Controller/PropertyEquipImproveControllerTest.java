package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove.dto.PropertyEquipImproveDto;

public class PropertyEquipImproveControllerTest extends CrudControllerTest<PropertyEquipImprove, PropertyEquipImproveDto, Long> {

    @Override
    protected PropertyEquipImprove createValidObject() {
        return PropertyEquipImprove.builder()
                .name("Teste")
                .type("Teste")
                .property(Property.builder().id(1L).build())
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