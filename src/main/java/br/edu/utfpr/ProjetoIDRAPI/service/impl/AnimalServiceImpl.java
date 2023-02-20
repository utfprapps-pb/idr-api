package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.repository.AnimalRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.AnimalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl extends CrudServiceImpl<Animal, Long> implements AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    protected JpaRepository<Animal, Long> getRepository() {
        return this.animalRepository;
    }

    @Override
    public Animal findByIdentifier(String identifier) {
        return animalRepository.findByIdentifier(identifier);
    }

}
