package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;

public interface AnimalService extends CrudService<Animal, Long> {

    Animal findByIdentifier(String identifier);

}
