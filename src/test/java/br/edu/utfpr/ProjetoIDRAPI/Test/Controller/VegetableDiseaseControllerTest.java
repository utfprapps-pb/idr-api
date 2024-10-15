package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetabledisease.dto.VegetableDiseaseDto;

public class VegetableDiseaseControllerTest extends CrudControllerTest<VegetableDisease, VegetableDiseaseDto, Long> {

    @Override
    protected VegetableDisease createValidObject() {
        return VegetableDisease.builder()
                .disease(Disease.builder().id(1L).build())
                .property(Property.builder().id(1L).build())
                .infestationType("Teste")
                .culture(Culture.builder().id(1L).build())
                .build();
    }

    @Override
    protected VegetableDisease createInvalidObject() {
        return VegetableDisease.builder().build();
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