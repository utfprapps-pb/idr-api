package br.gov.pr.idr.infra.iam.user.persistence;

import br.gov.pr.idr.domain.iam.user.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJPARepository extends JpaRepository<UserJPAEntity, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByUsername(String username);

    Optional<UserJPAEntity> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE (:active IS NULL OR u.active = :active) AND " +
            "(:terms = '' OR LOWER(u.name) LIKE LOWER(CONCAT('%', :terms, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :terms, '%')))")
    Page<UserJPAEntity> search(@Param("terms") String terms, @Param("active") Boolean active, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u JOIN u.userPermissions p WHERE u.active = true AND p.role = :role")
    List<UserJPAEntity> findAllActiveByPermissionRole(@Param("role") UserRole role);

    @Query("SELECT DISTINCT u FROM User u JOIN u.userPermissions p " +
            "WHERE u.active = true AND p.role = :role AND u.updatedAt > :since")
    List<UserJPAEntity> findActiveByPermissionRoleUpdatedAfter(@Param("role") UserRole role, @Param("since") Instant since);

    @Query("SELECT DISTINCT u FROM User u JOIN u.userPermissions p " +
            "WHERE u.active = false AND p.role = :role AND u.updatedAt > :since")
    List<UserJPAEntity> findInactiveByPermissionRoleUpdatedAfter(@Param("role") UserRole role, @Param("since") Instant since);
}
