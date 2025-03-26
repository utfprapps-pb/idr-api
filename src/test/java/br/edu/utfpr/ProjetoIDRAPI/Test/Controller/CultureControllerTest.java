package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.enums.CultureType;

public class CultureControllerTest extends CrudControllerTest<Culture, Culture, Long> {

    @Override
    protected Culture createValidObject() {
        return Culture.builder()
                .cultureName("Test")
                .cultureType(CultureType.Concentrado)
                .build();
    }

    @Override
    protected Culture createInvalidObject() {
        return Culture.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/cultures";
    }
}
