package br.gov.pr.idr.infra.livestock.animal_sales.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnimalSalesJPARepository extends JpaRepository<AnimalSalesJPAEntity, UUID> {

    Page<AnimalSalesJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Optional<AnimalSalesJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
