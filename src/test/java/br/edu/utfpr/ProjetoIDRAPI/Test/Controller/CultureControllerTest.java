package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class CultureControllerTest extends CrudControllerTest<Culture, Culture, Long> {

    @Autowired
    private CultureRepository cultureRepository;

    @Override
    protected Long persistAndReturnId(Culture entity) {
        return cultureRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        cultureRepository.deleteAll();
    }

    @Override
    protected Culture createValidObject() {
        return TestUtils.createValidCulture();
    }

    @Override
    protected Culture createInvalidObject() {
        return new Culture();
    }

    @Override
    protected String getURL() {
        return "/cultures";
    }

    @Override
    protected Class<Culture> getDtoClass() {
        return Culture.class;
    }
}