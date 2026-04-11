package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.feed.Feed;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.CultivationDisease;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.dto.CultivationDiseaseDto;

public class CultivationDiseaseControllerTest extends CrudControllerTest<CultivationDisease, CultivationDiseaseDto, Long> {

    @Override
    protected CultivationDisease createValidObject() {
        return CultivationDisease.builder()
                .disease(Disease.builder().id(1L).build())
                .property(Property.builder().id(1L).build())
                .infestationType("Teste")
                .feed(Feed.builder().id(1L).build())
                .build();
    }

    @Override
    protected CultivationDisease createInvalidObject() {
        return CultivationDisease.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/vegetablediseases";
    }
}