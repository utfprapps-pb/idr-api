package br.gov.pr.idr.infra.iam.refresh_token;

import br.gov.pr.idr.domain.iam.refresh_token.RefreshToken;
import br.gov.pr.idr.domain.iam.refresh_token.RefreshTokenID;
import br.gov.pr.idr.infra.iam.refresh_token.persistence.RefreshTokenJPAEntity;
import br.gov.pr.idr.infra.iam.refresh_token.persistence.RefreshTokenJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenPostgresGateway")
class RefreshTokenPostgresGatewayTest {

    @Mock RefreshTokenJPARepository repository;
    @InjectMocks RefreshTokenPostgresGateway gateway;

    private RefreshToken validToken() {
        final var now = Instant.now();
        return RefreshToken.with(RefreshTokenID.unique(), "token-abc",
                UUID.randomUUID(), "user", now.plus(7, ChronoUnit.DAYS), now, false);
    }

    private RefreshTokenJPAEntity validEntity() {
        final var now = Instant.now();
        return new RefreshTokenJPAEntity(UUID.randomUUID(), "token-abc",
                UUID.randomUUID(), "user", now.plus(7, ChronoUnit.DAYS), now, false);
    }

    @Test
    @DisplayName("deve salvar token e retornar domínio")
    void shouldSaveAndReturn() {
        final var entity = validEntity();
        when(repository.save(any())).thenReturn(entity);

        final var result = gateway.save(validToken());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve retornar token pelo valor quando encontrado")
    void shouldFindByToken() {
        final var entity = validEntity();
        when(repository.findByToken("token-abc")).thenReturn(Optional.of(entity));

        final var result = gateway.findByToken("token-abc");

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar lista de tokens por userId")
    void shouldFindAllByUserId() {
        final var entity = validEntity();
        when(repository.findAllByUserId(any())).thenReturn(List.of(entity));

        final var result = gateway.findAllByUserId(UUID.randomUUID());

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("deve deletar tokens expirados e revogados")
    void shouldDeleteExpiredAndRevoked() {
        gateway.deleteExpiredAndRevoked(Instant.now());

        verify(repository).deleteExpiredAndRevoked(any());
    }
}
