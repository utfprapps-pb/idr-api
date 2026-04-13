package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.DiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.dto.DiseaseDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DiseaseControllerTest extends CrudControllerTest<Disease, DiseaseDto, Long> {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Override
    protected Long persistAndReturnId(Disease entity) {
        return diseaseRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        diseaseRepository.deleteAll();
    }

    @Override
    protected Disease createValidObject() {
        return TestUtils.createValidDisease();
    }

    @Override
    protected Disease createInvalidObject() {
        return new Disease();
    }

    @Override
    protected String getURL() {
        return "/diseases";
    }

    @Override
    protected Class<DiseaseDto> getDtoClass() {
        return DiseaseDto.class;
    }
}