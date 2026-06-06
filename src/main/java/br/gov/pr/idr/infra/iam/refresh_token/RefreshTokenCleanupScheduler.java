package br.gov.pr.idr.infra.iam.refresh_token;

import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenGateway refreshTokenGateway;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void purgeExpiredAndRevoked() {
        refreshTokenGateway.deleteExpiredAndRevoked();
    }
}
