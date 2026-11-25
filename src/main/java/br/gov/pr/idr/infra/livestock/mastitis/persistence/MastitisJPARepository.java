package br.gov.pr.idr.infra.livestock.mastitis.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface MastitisJPARepository extends JpaRepository<MastitisJPAEntity, UUID> {

    Page<MastitisJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Page<MastitisJPAEntity> findAllByAnimalIdAndDiagnoseDate(UUID animalId, LocalDate diagnoseDate, Pageable pageable);

    Optional<MastitisJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
