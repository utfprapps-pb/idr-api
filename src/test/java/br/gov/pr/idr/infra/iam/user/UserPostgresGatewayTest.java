package br.gov.pr.idr.infra.iam.user;

import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.query.SearchUserQuery;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserPostgresGateway")
class UserPostgresGatewayTest {

    @Mock UserJPARepository repository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UserJPAEntity entityMock;
    @InjectMocks UserPostgresGateway gateway;

    private User validUser() {
        final var now = Instant.now();
        return User.with(
                UserID.unique(), "João Silva", "joao.silva",
                Password.from("Senha@123", "Senha@123"),
                CPF.from("529.982.247-25"),
                "41999999999",
                CityID.unique(),
                "80000-000", "Rua das Flores", "123", "CREA-1234", "2010",
                now, now, false, Set.of()
        );
    }

    @Test
    @DisplayName("deve criar usuário com senha codificada")
    void shouldCreateUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(validUser());

        final var result = gateway.create(validUser());

        assertNotNull(result);
        verify(passwordEncoder).encode(anyString());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve atualizar usuário")
    void shouldUpdateUser() {
        when(repository.save(any())).thenReturn(entityMock);
        when(entityMock.toDomain()).thenReturn(validUser());

        final var result = gateway.update(validUser());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("deve verificar existência por CPF")
    void shouldCheckExistsByCPF() {
        when(repository.existsByCpf(anyString())).thenReturn(true);

        assertTrue(gateway.existsByCPF(CPF.from("529.982.247-25")));
    }

    @Test
    @DisplayName("deve verificar existência por username")
    void shouldCheckExistsByUsername() {
        when(repository.existsByUsername("joao")).thenReturn(false);

        assertFalse(gateway.existsByUsername("joao"));
    }

    @Test
    @DisplayName("deve verificar existência por ID")
    void shouldCheckExistsById() {
        final var id = UserID.unique();
        when(repository.existsById(id.id())).thenReturn(true);

        assertTrue(gateway.existsById(id));
    }

    @Test
    @DisplayName("deve retornar Optional com usuário quando encontrado por username")
    void shouldFindByUsernameWhenFound() {
        when(repository.findByUsername("joao")).thenReturn(Optional.of(entityMock));
        when(entityMock.toDomain()).thenReturn(validUser());

        final var result = gateway.findByUsername("joao");

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar Optional vazio quando usuário não encontrado por username")
    void shouldReturnEmptyWhenUsernameNotFound() {
        when(repository.findByUsername("inexistente")).thenReturn(Optional.empty());

        final var result = gateway.findByUsername("inexistente");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deve retornar Optional com usuário quando encontrado por ID")
    void shouldFindByIdWhenFound() {
        final var id = UserID.unique();
        when(repository.findById(id.id())).thenReturn(Optional.of(entityMock));
        when(entityMock.toDomain()).thenReturn(validUser());

        final var result = gateway.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("deve retornar paginação de usuários na busca")
    void shouldReturnPaginationOnSearch() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.search(any(), any(), any(Pageable.class))).thenReturn(page);
        when(entityMock.toDomain()).thenReturn(validUser());

        final var query = SearchUserQuery.from(0, 10, "", "name", "asc", null);
        final var result = gateway.search(query);

        assertEquals(1, result.total());
    }

    @Test
    @DisplayName("deve retornar paginação de usuários na busca com termos nulos")
    void shouldReturnPaginationOnSearchWithNullTerms() {
        final var page = new PageImpl<>(List.of(entityMock));
        when(repository.search(eq(""), any(), any(Pageable.class))).thenReturn(page);
        when(entityMock.toDomain()).thenReturn(validUser());

        final var query = SearchUserQuery.from(0, 10, null, "name", "asc", null);
        final var result = gateway.search(query);

        assertEquals(1, result.total());
        verify(repository).search(eq(""), any(), any(Pageable.class));
    }

    @Test
    @DisplayName("deve atualizar senha do usuário")
    void shouldUpdatePassword() {
        when(repository.save(any())).thenReturn(entityMock);

        gateway.updatePassword(validUser(), "encodedPassword");

        verify(repository).save(any());
    }
}
