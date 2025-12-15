package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.breed.Breed;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;

public class AnimalControllerTest extends CrudControllerTest<Animal, AnimalDto, Long> {

    @Override
    protected Animal createValidObject() {
        return Animal.builder()
                .property(Property.builder().id(1L).build())
                .animalMother(Animal.builder().id(1L).build())
                .breed(Breed.builder().id(1L).build())
                .build();
    }

    @Override
    protected Animal createInvalidObject() {
        return Animal.builder().build();
    }

    @Override
    protected Long getValidId() {
        return 1L;
    }

    @Override
    protected String getURL() {
        return "/animals";
    }
}
