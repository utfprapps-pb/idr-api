package br.gov.pr.idr.domain.iam.refresh_token;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenGateway {

    RefreshToken save(final RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(final String token);

    void revokeAllByUserId(final UUID userId);

    void deleteExpiredAndRevoked();
}
