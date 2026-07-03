package br.gov.pr.idr.application.iam.refresh_token.issue;

import br.gov.pr.idr.application.iam.refresh_token.revoke.RevokeRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.revoke.RevokeRefreshTokenUseCase;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;

@CommandUseCase
public class IssueRefreshTokenUseCase extends UseCase<IssueRefreshTokenCommand, IssueRefreshTokenOutput> {

    private final RefreshTokenGateway refreshTokenGateway;
    private final RevokeRefreshTokenUseCase revokeRefreshTokenUseCase;

    public IssueRefreshTokenUseCase(final RefreshTokenGateway refreshTokenGateway,
                                    final RevokeRefreshTokenUseCase revokeRefreshTokenUseCase) {
        this.refreshTokenGateway = refreshTokenGateway;
        this.revokeRefreshTokenUseCase = revokeRefreshTokenUseCase;
    }

    @Override
    public IssueRefreshTokenOutput execute(final IssueRefreshTokenCommand command) {
        revokeRefreshTokenUseCase.execute(RevokeRefreshTokenCommand.from(command.userId()));
        final var entity = RefreshToken.create(command.userId(), command.username(), command.expirationDays());
        final var refreshToken = refreshTokenGateway.save(entity);
        return IssueRefreshTokenOutput.from(refreshToken);
    }
}
