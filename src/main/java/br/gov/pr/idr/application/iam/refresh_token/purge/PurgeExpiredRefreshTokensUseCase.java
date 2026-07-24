package br.gov.pr.idr.application.iam.refresh_token.purge;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;

import java.time.Instant;

@CommandUseCase
public class PurgeExpiredRefreshTokensUseCase extends VoidUseCase<Instant> {

    private final RefreshTokenGateway refreshTokenGateway;

    public PurgeExpiredRefreshTokensUseCase(final RefreshTokenGateway refreshTokenGateway) {
        this.refreshTokenGateway = refreshTokenGateway;
    }

    public void execute(final Instant deleteBeforeAt) {
        refreshTokenGateway.deleteExpiredAndRevoked(deleteBeforeAt);
    }
}
