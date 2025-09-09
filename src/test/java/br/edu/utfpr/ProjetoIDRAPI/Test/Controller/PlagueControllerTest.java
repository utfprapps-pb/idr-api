package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.dto.PlagueDto;

public class PlagueControllerTest extends CrudControllerTest<Plague, PlagueDto, Long> {

    @Override
    protected Plague createValidObject() {
        return Plague.builder()
                .plagueName("Teste")
                .build();
    }

    protected Plague createInvalidObject() {
        return Plague.builder().build();
    }

    protected Long getValidId() {
        return 1L;
    }

    protected String getURL() {
        return "/plagues";
    }
}
