package br.gov.pr.idr.application.iam.refresh_token.issue;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;

@CommandUseCase
public class IssueRefreshTokenUseCase extends UseCase<IssueRefreshTokenCommand, IssueRefreshTokenOutput> {

    private final RefreshTokenGateway refreshTokenGateway;

    public IssueRefreshTokenUseCase(final RefreshTokenGateway refreshTokenGateway) {
        this.refreshTokenGateway = refreshTokenGateway;
    }

    @Override
    public IssueRefreshTokenOutput execute(final IssueRefreshTokenCommand command) {
        refreshTokenGateway.revokeAllByUserId(command.userId());
        RefreshToken entity = RefreshToken.create(command.userId(),
                                                         command.username(),
                                                         command.expirationDays());
        final var refreshToken = refreshTokenGateway.save(entity);
        return IssueRefreshTokenOutput.from(refreshToken);
    }
}
