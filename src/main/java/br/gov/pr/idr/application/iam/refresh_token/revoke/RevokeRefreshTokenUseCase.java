package br.gov.pr.idr.application.iam.refresh_token.revoke;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.VoidUseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;

@CommandUseCase
public class RevokeRefreshTokenUseCase extends VoidUseCase<RevokeRefreshTokenCommand> {

    private final RefreshTokenGateway refreshTokenGateway;

    public RevokeRefreshTokenUseCase(final RefreshTokenGateway refreshTokenGateway) {
        this.refreshTokenGateway = refreshTokenGateway;
    }

    @Override
    public void execute(final RevokeRefreshTokenCommand command) {
        refreshTokenGateway.findAllByUserId(command.userId())
                .forEach(token -> {
                    token.revoke();
                    refreshTokenGateway.save(token);
                });
    }
}
