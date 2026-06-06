package br.gov.pr.idr.infra.iam.refresh_token;

import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import br.gov.pr.idr.infra.iam.refresh_token.persistence.RefreshTokenJPAEntity;
import br.gov.pr.idr.infra.iam.refresh_token.persistence.RefreshTokenJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenPostgresGateway implements RefreshTokenGateway {

    private final RefreshTokenJPARepository repository;

    @Override
    public RefreshToken save(final RefreshToken refreshToken) {
        return repository.save(RefreshTokenJPAEntity.from(refreshToken)).toDomain();
    }

    @Override
    public Optional<RefreshToken> findByToken(final String token) {
        return repository.findByToken(token).map(RefreshTokenJPAEntity::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUserId(final UUID userId) {
        repository.revokeAllByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteExpiredAndRevoked() {
        repository.deleteExpiredAndRevoked(Instant.now());
    }
}
