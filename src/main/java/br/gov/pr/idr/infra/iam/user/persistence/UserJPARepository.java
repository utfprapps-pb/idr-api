package br.gov.pr.idr.infra.iam.user.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJPARepository extends JpaRepository<UserJPAEntity, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByUsername(String username);

    Optional<UserJPAEntity> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.active = :active AND " +
            "(:terms = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :terms, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :terms, '%')))")
    Page<UserJPAEntity> search(@Param("terms") String terms, @Param("active") boolean active, Pageable pageable);
}
