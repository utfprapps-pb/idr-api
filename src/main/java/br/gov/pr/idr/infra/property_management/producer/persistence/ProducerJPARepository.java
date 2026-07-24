package br.gov.pr.idr.infra.property_management.producer.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProducerJPARepository extends JpaRepository<ProducerJPAEntity, UUID> {

    boolean existsByCpf(String cpf);

    Optional<ProducerJPAEntity> findByCpf(String cpf);

    @Query("SELECT p FROM Producer p WHERE :terms = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%')) OR " +
            "LOWER(p.cpf) LIKE LOWER(CONCAT('%', :terms, '%'))")
    Page<ProducerJPAEntity> search(@Param("terms") String terms, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Producer p WHERE EXISTS " +
            "(SELECT pr FROM Property pr WHERE pr.producerId = p.id AND pr.cityId IN :cityIds)")
    List<ProducerJPAEntity> findAllByCityIds(@Param("cityIds") Set<UUID> cityIds);

    @Query("SELECT DISTINCT p FROM Producer p WHERE p.updatedAt > :since AND EXISTS " +
            "(SELECT pr FROM Property pr WHERE pr.producerId = p.id AND pr.cityId IN :cityIds)")
    List<ProducerJPAEntity> findAllByCityIdsAndUpdatedAtAfter(@Param("cityIds") Set<UUID> cityIds,
                                                              @Param("since") Instant since);

    @Query(value = "SELECT DISTINCT p.* FROM producer p WHERE p.deleted_at IS NOT NULL AND p.deleted_at > :since " +
            "AND EXISTS (SELECT 1 FROM property pr WHERE pr.producer_id = p.id AND pr.city_id IN (:cityIds))",
            nativeQuery = true)
    List<ProducerJPAEntity> findDeletedSince(@Param("cityIds") Set<UUID> cityIds, @Param("since") Instant since);
}
