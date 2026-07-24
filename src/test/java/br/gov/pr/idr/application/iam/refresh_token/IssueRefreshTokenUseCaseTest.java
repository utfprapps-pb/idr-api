package br.gov.pr.idr.application.iam.refresh_token;

import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenCommand;
import br.gov.pr.idr.application.iam.refresh_token.issue.IssueRefreshTokenUseCase;
import br.gov.pr.idr.application.iam.refresh_token.revoke.RevokeRefreshTokenUseCase;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IssueRefreshTokenUseCase")
class IssueRefreshTokenUseCaseTest {

    @Mock RefreshTokenGateway refreshTokenGateway;
    @Mock RevokeRefreshTokenUseCase revokeRefreshTokenUseCase;
    @InjectMocks IssueRefreshTokenUseCase useCase;

    @Test
    @DisplayName("deve revogar tokens existentes, criar novo token e retornar output")
    void shouldRevokeExistingAndIssueNew() {
        final var userId = UUID.randomUUID();
        final var token = RefreshToken.create(userId, "joao", 7L);
        when(refreshTokenGateway.save(any())).thenReturn(token);

        final var command = IssueRefreshTokenCommand.from(userId, "joao", 7L);
        final var output = useCase.execute(command);

        assertNotNull(output);
        assertNotNull(output.refreshToken());
        verify(revokeRefreshTokenUseCase).execute(any());
        verify(refreshTokenGateway).save(any());
    }
}
