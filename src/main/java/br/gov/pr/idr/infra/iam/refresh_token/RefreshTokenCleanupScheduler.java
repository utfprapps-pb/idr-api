package br.gov.pr.idr.infra.iam.refresh_token;

import br.gov.pr.idr.application.iam.refresh_token.purge.PurgeExpiredRefreshTokensUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final PurgeExpiredRefreshTokensUseCase purgeExpiredRefreshTokensUseCase;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void purgeExpiredAndRevoked() {
        purgeExpiredRefreshTokensUseCase.execute(Instant.now());
    }
}
