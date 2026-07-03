package br.gov.pr.idr.application.iam.refresh_token;

import br.gov.pr.idr.application.iam.refresh_token.revoke.RevokeRefreshTokenUseCase;
import br.gov.pr.idr.application.iam.refresh_token.rotate.RotateRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.rotate.RotateRefreshTokenUseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RotateRefreshTokenUseCase")
class RotateRefreshTokenUseCaseTest {

    @Mock RefreshTokenGateway refreshTokenGateway;
    @Mock RevokeRefreshTokenUseCase revokeRefreshTokenUseCase;
    @InjectMocks RotateRefreshTokenUseCase useCase;

    @Test
    @DisplayName("deve rotacionar token válido e retornar novo token")
    void shouldRotateValidToken() {
        final var existing = RefreshToken.create(UUID.randomUUID(), "joao", 7L);
        final var newToken = RefreshToken.create(existing.getUserId(), "joao", 7L);
        when(refreshTokenGateway.findByToken("valid-token")).thenReturn(Optional.of(existing));
        when(refreshTokenGateway.save(any())).thenReturn(newToken);

        final var output = useCase.execute(RotateRefreshTokenCommand.from("valid-token", 7L));

        assertNotNull(output);
        verify(revokeRefreshTokenUseCase).execute(any());
        verify(refreshTokenGateway).save(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando token não é encontrado")
    void shouldThrowWhenTokenNotFound() {
        when(refreshTokenGateway.findByToken("unknown")).thenReturn(Optional.empty());

        assertThrows(NotificationException.class,
                () -> useCase.execute(RotateRefreshTokenCommand.from("unknown", 7L)));
    }

    @Test
    @DisplayName("deve lançar exceção quando token está revogado")
    void shouldThrowWhenTokenRevoked() {
        final var revoked = RefreshToken.with(RefreshTokenID.unique(), "tok",
                UUID.randomUUID(), "joao", Instant.now().plus(7, ChronoUnit.DAYS), Instant.now(), true);
        when(refreshTokenGateway.findByToken("tok")).thenReturn(Optional.of(revoked));

        assertThrows(NotificationException.class,
                () -> useCase.execute(RotateRefreshTokenCommand.from("tok", 7L)));
    }

    @Test
    @DisplayName("deve lançar exceção quando token está expirado")
    void shouldThrowWhenTokenExpired() {
        final var expired = RefreshToken.with(RefreshTokenID.unique(), "tok",
                UUID.randomUUID(), "joao", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(), false);
        when(refreshTokenGateway.findByToken("tok")).thenReturn(Optional.of(expired));

        assertThrows(NotificationException.class,
                () -> useCase.execute(RotateRefreshTokenCommand.from("tok", 7L)));
    }
}
