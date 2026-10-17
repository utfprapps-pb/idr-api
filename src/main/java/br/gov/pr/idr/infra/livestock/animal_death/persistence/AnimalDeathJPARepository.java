package br.gov.pr.idr.infra.livestock.animal_death.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnimalDeathJPARepository extends JpaRepository<AnimalDeathJPAEntity, UUID> {

    boolean existsByAnimalId(UUID animalId);

    Page<AnimalDeathJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);
}
