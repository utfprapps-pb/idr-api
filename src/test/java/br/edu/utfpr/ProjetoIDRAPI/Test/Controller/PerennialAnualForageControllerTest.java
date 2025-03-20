package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudDependentControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.dto.PerennialAnualForageDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;

public class PerennialAnualForageControllerTest extends CrudDependentControllerTest<Property, PerennialAnualForage, PerennialAnualForageDto, Long> {

    @Override
    protected Property createValidDependencyObject() {
        return Property.builder()
                .user(User.builder().id(1L).build())
                .leased(false)
                .build();
    }

    @Override
    protected PerennialAnualForage createValidObject(Property property) {
        return PerennialAnualForage.builder()
                .property(property)
                .build();
    }

    @Override
    protected PerennialAnualForage createValidObject() {
        return null;
    }

    @Override
    protected PerennialAnualForage createInvalidObject() {
        return PerennialAnualForage.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/fodders";
    }

    @Override
    protected String getDependencyURL() {
        return "/properties";
    }
}