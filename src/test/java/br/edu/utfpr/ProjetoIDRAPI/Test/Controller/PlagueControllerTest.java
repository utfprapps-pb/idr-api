package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.PlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.dto.PlagueDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class PlagueControllerTest extends CrudControllerTest<Plague, PlagueDto, Long> {

    @Autowired
    private PlagueRepository plagueRepository;

    @Override
    protected Long persistAndReturnId(Plague entity) {
        return plagueRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        plagueRepository.deleteAll();
    }

    @Override
    protected Plague createValidObject() {
        return TestUtils.createValidPlague();
    }

    @Override
    protected Plague createInvalidObject() {
        return new Plague();
    }

    @Override
    protected String getURL() {
        return "/plagues";
    }

    @Override
    protected Class<PlagueDto> getDtoClass() {
        return PlagueDto.class;
    }
}