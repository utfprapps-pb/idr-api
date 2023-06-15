package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;

import java.util.List;

public interface AnimalService extends CrudService<Animal, Long> {

    Animal findByIdentifier(String identifier);

    boolean saveListAnimals(List<Animal> animals);

}
