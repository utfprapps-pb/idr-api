package br.gov.pr.idr.infra.iam.email;

import br.gov.pr.idr.domain.iam.email.Email;
import br.gov.pr.idr.domain.iam.email.EmailID;
import br.gov.pr.idr.infra.iam.email.persistence.EmailJPAEntity;
import br.gov.pr.idr.infra.iam.email.persistence.EmailJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailPostgresGateway")
class EmailPostgresGatewayTest {

    @Mock EmailJPARepository repository;
    @Mock EmailJPAEntity entityMock;
    @InjectMocks EmailPostgresGateway gateway;

    private Email validEmail() {
        final var now = Instant.now();
        return Email.with(EmailID.unique(), "ABCD1234", "user@test.com", "user",
                now, now.plus(5, ChronoUnit.MINUTES));
    }

    @Test
    @DisplayName("deve salvar email e retornar domínio")
    void shouldSaveAndReturn() {
        final var email = validEmail();
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(email);

        final var result = gateway.save(email);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve deletar por email")
    void shouldDeleteByEmail() {
        gateway.deleteByEmail("user@test.com");

        verify(repository).deleteByRecoveryEmail("user@test.com");
    }

    @Test
    @DisplayName("deve retornar Optional com email quando encontrado")
    void shouldFindByEmailAndCode() {
        final var email = validEmail();
        when(repository.findByRecoveryEmailAndRecoveryCode(anyString(), anyString()))
                .thenReturn(Optional.of(entityMock));
        when(entityMock.toDomain()).thenReturn(email);

        final var result = gateway.findByEmailAndCode("user@test.com", "ABCD1234");

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando não encontrado")
    void shouldReturnEmptyWhenNotFound() {
        when(repository.findByRecoveryEmailAndRecoveryCode(anyString(), anyString()))
                .thenReturn(Optional.empty());

        final var result = gateway.findByEmailAndCode("user@test.com", "INVALIDO");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("EmailJPAEntity.toDomain deve reconstituir domínio corretamente")
    void shouldMapToDomainCorrectly() {
        final var email = validEmail();
        final var entity = EmailJPAEntity.from(email);

        final var domain = entity.toDomain();

        assertEquals(email.getId().id(), domain.getId().id());
        assertEquals(email.getCode(), domain.getCode());
        assertEquals(email.getEmail(), domain.getEmail());
        assertEquals(email.getUserName(), domain.getUserName());
    }
}
