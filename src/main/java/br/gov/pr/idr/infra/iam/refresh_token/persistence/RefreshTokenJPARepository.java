package br.gov.pr.idr.infra.iam.refresh_token.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJPARepository extends JpaRepository<RefreshTokenJPAEntity, UUID> {

    Optional<RefreshTokenJPAEntity> findByToken(String token);

    List<RefreshTokenJPAEntity> findAllByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :deleteBeforeAt OR t.revoked = true")
    void deleteExpiredAndRevoked(Instant deleteBeforeAt);
}
