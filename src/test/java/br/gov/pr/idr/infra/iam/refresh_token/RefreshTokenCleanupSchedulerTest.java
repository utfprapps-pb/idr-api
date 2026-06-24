package br.gov.pr.idr.infra.iam.refresh_token;

import br.gov.pr.idr.application.iam.refresh_token.purge.PurgeExpiredRefreshTokensUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenCleanupScheduler")
class RefreshTokenCleanupSchedulerTest {

    @Mock PurgeExpiredRefreshTokensUseCase purgeExpiredRefreshTokensUseCase;
    @InjectMocks RefreshTokenCleanupScheduler scheduler;

    @Test
    @DisplayName("deve delegar purge ao use case com Instant.now()")
    void shouldDelegatePurgeToUseCase() {
        scheduler.purgeExpiredAndRevoked();

        verify(purgeExpiredRefreshTokensUseCase).execute(any());
    }
}
