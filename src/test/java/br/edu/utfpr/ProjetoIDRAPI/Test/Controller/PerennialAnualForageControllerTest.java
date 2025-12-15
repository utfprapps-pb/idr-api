package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage.dto.PerennialAnualForageDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;

public class PerennialAnualForageControllerTest extends CrudControllerTest<PerennialAnualForage, PerennialAnualForageDto, Long> {

    @Override
    protected PerennialAnualForage createValidObject() {
        return PerennialAnualForage.builder()
                .property(Property.builder().id(1L).build())
                .build();
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
}