package br.gov.pr.idr.infra.livestock.animal_diseases.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AnimalDiseasesJPARepository extends JpaRepository<AnimalDiseasesJPAEntity, UUID> {

    Page<AnimalDiseasesJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Page<AnimalDiseasesJPAEntity> findAllByAnimalIdAndDiagnosisDate(UUID animalId, LocalDate diagnosisDate, Pageable pageable);

    Optional<AnimalDiseasesJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
