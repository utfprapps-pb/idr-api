package br.gov.pr.idr.infra.livestock.breed.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BreedJPARepository extends JpaRepository<BreedJPAEntity, UUID>,
        JpaSpecificationExecutor<BreedJPAEntity> {

    /** Projeção fechada: seleciona só id e nome. */
    List<BreedNameView> findByIdIn(Collection<UUID> ids);

    interface BreedNameView {
        UUID getId();

        String getBreedName();
    }
}
