package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.Pest;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.dto.PestDto;

public class PestControllerTest extends CrudControllerTest<Pest, PestDto, Long> {

    @Override
    protected Pest createValidObject() {
        return Pest.builder()
                .name("Teste")
                .build();
    }

    protected Pest createInvalidObject() {
        return Pest.builder().build();
    }

    protected Long getValidId() {
        return 1L;
    }

    protected String getURL() {
        return "/pests";
    }
}
