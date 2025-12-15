package br.edu.utfpr.ProjetoIDRAPI.entity.animal;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface AnimalService extends CrudService<Animal, Long> {

    Animal findByIdentifier(String identifier);

    boolean saveListAnimals(List<Animal> animals);

}
