package br.gov.pr.idr.infra.property_management.producer.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProducerJPARepository extends JpaRepository<ProducerJPAEntity, UUID> {

    boolean existsByCpf(String cpf);

    @Query("SELECT p FROM Producer p WHERE :terms = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%')) OR " +
            "LOWER(p.cpf) LIKE LOWER(CONCAT('%', :terms, '%'))")
    Page<ProducerJPAEntity> search(@Param("terms") String terms, Pageable pageable);
}
