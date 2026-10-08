package br.gov.pr.idr.legacy.entity.animal.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import br.gov.pr.idr.legacy.entity.animal.Animal;
import br.gov.pr.idr.legacy.entity.animal.AnimalRepository;
import br.gov.pr.idr.legacy.entity.animal.AnimalService;
import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnimalServiceImpl extends CrudServiceImpl<Animal, UUID> implements AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    protected JpaRepository<Animal, UUID> getRepository() {
        return this.animalRepository;
    }

    @Override
    public Animal findByIdentifier(String identifier) {
        return animalRepository.findByIdentifier(identifier);
    }

    @Override
    public Animal update(final UUID id, final Animal entity) {
        final Animal existing = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entity with id " + id + " not found"));
        entity.setPropertyId(existing.getPropertyId());
        return super.update(id, entity);
    }

    @Override
    public boolean saveListAnimals(List<Animal> animals) {

        boolean status = true;
        try {
            animalRepository.saveAll(animals);
        } catch (Exception e){
            status = false;
            log.error(e.getMessage());
        }

        return status;
    }

    @Override
    public JpaSpecificationExecutor<Animal> getSpecExecutor() {
        return this.animalRepository;
    }
}
