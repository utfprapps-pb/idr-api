package br.gov.pr.idr.infra.inventory_resource.medication.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MedicationJPARepository extends JpaRepository<MedicationJPAEntity, UUID> {

    Page<MedicationJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Page<MedicationJPAEntity> findAllByAnimalIdAndApplicationDate(UUID animalId, LocalDate applicationDate, Pageable pageable);

    Optional<MedicationJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
