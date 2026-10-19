package br.gov.pr.idr.infra.livestock.insemination.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InseminationJPARepository extends JpaRepository<InseminationJPAEntity, UUID> {

    Page<InseminationJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);

    Optional<InseminationJPAEntity> findByIdAndAnimalId(UUID id, UUID animalId);
}
