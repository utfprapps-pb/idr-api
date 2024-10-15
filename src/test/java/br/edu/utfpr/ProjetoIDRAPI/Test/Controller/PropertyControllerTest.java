package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;

public class PropertyControllerTest extends CrudControllerTest<Property, PropertyDto, Long> {

    @Override
    protected Property createValidObject() {
        return Property.builder()
                .user(User.builder().id(1L).build())
                .leased(false)
                .build();
    }

    @Override
    protected Property createInvalidObject() {
        return Property.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/properties";
    }
}