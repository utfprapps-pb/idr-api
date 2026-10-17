package br.gov.pr.idr.infra.livestock.mastitis.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MastitisJPARepository extends JpaRepository<MastitisJPAEntity, UUID> {

    Page<MastitisJPAEntity> findAllByAnimalId(UUID animalId, Pageable pageable);
}
