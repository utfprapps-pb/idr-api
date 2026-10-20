package br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PregnancyDiagnoseJPARepository extends JpaRepository<PregnancyDiagnoseJPAEntity, UUID> {

    Page<PregnancyDiagnoseJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Optional<PregnancyDiagnoseJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
