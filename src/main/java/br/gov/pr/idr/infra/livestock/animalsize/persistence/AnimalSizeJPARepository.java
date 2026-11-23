package br.gov.pr.idr.infra.livestock.animalsize.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AnimalSizeJPARepository extends JpaRepository<AnimalSizeJPAEntity, UUID>,
        JpaSpecificationExecutor<AnimalSizeJPAEntity> {

    /** Projeção fechada: seleciona só id e nome. */
    List<AnimalSizeNameView> findByIdIn(Collection<UUID> ids);

    interface AnimalSizeNameView {
        UUID getId();

        String getSizeName();
    }
}
