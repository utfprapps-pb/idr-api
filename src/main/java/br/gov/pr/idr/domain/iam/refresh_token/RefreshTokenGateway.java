package br.gov.pr.idr.domain.iam.refresh_token;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenGateway {

    RefreshToken save(final RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(final String token);

    List<RefreshToken> findAllByUserId(final UUID userId);

    void deleteExpiredAndRevoked(final Instant now);
}
