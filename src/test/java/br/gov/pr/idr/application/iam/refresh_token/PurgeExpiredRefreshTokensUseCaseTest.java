package br.gov.pr.idr.application.iam.refresh_token;

import br.gov.pr.idr.application.iam.refresh_token.purge.PurgeExpiredRefreshTokensUseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PurgeExpiredRefreshTokensUseCase")
class PurgeExpiredRefreshTokensUseCaseTest {

    @Mock RefreshTokenGateway refreshTokenGateway;
    @InjectMocks PurgeExpiredRefreshTokensUseCase useCase;

    @Test
    @DisplayName("deve delegar purge ao gateway com o instant informado")
    void shouldDelegatePurgeToGateway() {
        final var now = Instant.now();
        useCase.execute(now);
        verify(refreshTokenGateway).deleteExpiredAndRevoked(now);
    }
}
