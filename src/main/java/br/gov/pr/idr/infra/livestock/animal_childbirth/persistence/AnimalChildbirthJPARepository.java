package br.gov.pr.idr.infra.livestock.animal_childbirth.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnimalChildbirthJPARepository extends JpaRepository<AnimalChildbirthJPAEntity, UUID> {

    Page<AnimalChildbirthJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Optional<AnimalChildbirthJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
