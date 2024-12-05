package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.dto.DiseaseDto;

public class DiseaseControllerTest extends CrudControllerTest<Disease, DiseaseDto, Long> {

    @Override
    protected Disease createValidObject() {
        return Disease.builder()
                .diseaseName("Teste")
                .build();
    }

    protected Disease createInvalidObject() {
        return Disease.builder().build();
    }

    protected Long getValidId() {
        return 1L;
    }

    protected String getURL() {
        return "/diseases";
    }
}
