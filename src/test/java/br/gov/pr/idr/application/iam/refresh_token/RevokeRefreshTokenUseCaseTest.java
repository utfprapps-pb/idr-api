package br.gov.pr.idr.application.iam.refresh_token;

import br.gov.pr.idr.application.iam.refresh_token.revoke.RevokeRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.revoke.RevokeRefreshTokenUseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RevokeRefreshTokenUseCase")
class RevokeRefreshTokenUseCaseTest {

    @Mock RefreshTokenGateway refreshTokenGateway;
    @InjectMocks RevokeRefreshTokenUseCase useCase;

    @Test
    @DisplayName("deve revogar todos os tokens do usuário")
    void shouldRevokeAllUserTokens() {
        final var userId = UUID.randomUUID();
        final var token1 = RefreshToken.create(userId, "joao", 7L);
        final var token2 = RefreshToken.create(userId, "joao", 7L);
        when(refreshTokenGateway.findAllByUserId(userId)).thenReturn(List.of(token1, token2));
        when(refreshTokenGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(RevokeRefreshTokenCommand.from(userId));

        verify(refreshTokenGateway, times(2)).save(argThat(RefreshToken::isRevoked));
    }

    @Test
    @DisplayName("não deve salvar quando usuário não tem tokens")
    void shouldDoNothingWhenNoTokens() {
        final var userId = UUID.randomUUID();
        when(refreshTokenGateway.findAllByUserId(userId)).thenReturn(List.of());

        useCase.execute(RevokeRefreshTokenCommand.from(userId));

        verify(refreshTokenGateway, never()).save(any());
    }
}
