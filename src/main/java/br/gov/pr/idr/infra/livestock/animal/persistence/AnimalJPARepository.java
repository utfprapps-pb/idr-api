package br.gov.pr.idr.infra.livestock.animal.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimalJPARepository extends JpaRepository<AnimalJPAEntity, UUID> {

    Page<AnimalJPAEntity> findAllByPropertyIdAndIdentifierContainingIgnoreCase(UUID propertyId, String terms,
                                                                                Pageable pageable);

    Optional<AnimalJPAEntity> findByPropertyIdAndIdentifier(UUID propertyId, String identifier);

    boolean existsByIdAndPropertyId(UUID id, UUID propertyId);

    /** Projeção fechada: seleciona só id e identificador. */
    List<AnimalIdentifierView> findByIdIn(Collection<UUID> ids);

    interface AnimalIdentifierView {
        UUID getId();

        String getIdentifier();
    }
}
