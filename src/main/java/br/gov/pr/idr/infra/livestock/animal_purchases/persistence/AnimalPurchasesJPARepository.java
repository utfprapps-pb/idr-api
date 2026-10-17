package br.gov.pr.idr.infra.livestock.animal_purchases.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnimalPurchasesJPARepository extends JpaRepository<AnimalPurchasesJPAEntity, UUID> {

    Page<AnimalPurchasesJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);
}
