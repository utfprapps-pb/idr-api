package br.gov.pr.idr.application.iam.refresh_token.rotate;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import br.gov.pr.idr.domain.shared.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

@CommandUseCase
public class RotateRefreshTokenUseCase extends UseCase<RotateRefreshTokenCommand, RotateRefreshTokenOutput> {

    private final RefreshTokenGateway refreshTokenGateway;

    public RotateRefreshTokenUseCase(final RefreshTokenGateway refreshTokenGateway) {
        this.refreshTokenGateway = refreshTokenGateway;
    }

    @Override
    public RotateRefreshTokenOutput execute(final RotateRefreshTokenCommand command) {
        NotificationValidation notification = NotificationValidation.create();
        final var existing = refreshTokenGateway.findByToken(command.token())
                                                .orElseThrow(() -> new NotificationException(
                                                        "Refresh token não encontrado.", notification));
        if (existing.isRevoked() || existing.isExpired()) {
            throw new NotificationException(
                    "Refresh token inválido ou expirado.", notification);
        }
        refreshTokenGateway.revokeAllByUserId(existing.getUserId());
        RefreshToken entity = RefreshToken.create(existing.getUserId(),
                                                        existing.getUsername(),
                                                        command.expirationDays());
        final var newToken = refreshTokenGateway.save(entity);
        return RotateRefreshTokenOutput.from(existing.getUsername(), newToken);
    }
}
