package br.gov.pr.idr.legacy.entity.animal;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface AnimalService extends CrudService<Animal, Long> {

    Animal findByIdentifier(String identifier);

    boolean saveListAnimals(List<Animal> animals);

}
