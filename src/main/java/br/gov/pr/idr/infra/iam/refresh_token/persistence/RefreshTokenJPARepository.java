package br.gov.pr.idr.infra.iam.refresh_token.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJPARepository extends JpaRepository<RefreshTokenJPAEntity, UUID> {

    Optional<RefreshTokenJPAEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.userId = :userId")
    void revokeAllByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now OR t.revoked = true")
    void deleteExpiredAndRevoked(Instant now);
}
