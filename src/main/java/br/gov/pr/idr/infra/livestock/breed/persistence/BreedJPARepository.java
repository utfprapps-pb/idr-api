package br.gov.pr.idr.infra.livestock.breed.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BreedJPARepository extends JpaRepository<BreedJPAEntity, UUID>,
        JpaSpecificationExecutor<BreedJPAEntity> {
}
