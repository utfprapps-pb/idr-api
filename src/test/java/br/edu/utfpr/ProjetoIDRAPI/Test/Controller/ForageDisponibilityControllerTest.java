package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;

public class ForageDisponibilityControllerTest extends CrudControllerTest<ForageDisponibility, ForageDisponibilityDto, Long> {

    @Override
    protected ForageDisponibility createValidObject() {
        return ForageDisponibility.builder()
                .property(Property.builder().id(1L).build())
                .build();
    }

    @Override
    protected ForageDisponibility createInvalidObject() {
        return ForageDisponibility.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/foragesDisponibilities";
    }
}