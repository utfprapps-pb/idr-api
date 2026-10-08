package br.gov.pr.idr.legacy.entity.animal;

import java.util.List;
import java.util.UUID;
import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface AnimalService extends CrudService<Animal, UUID> {

    Animal findByIdentifier(String identifier);

    boolean saveListAnimals(List<Animal> animals);

}
