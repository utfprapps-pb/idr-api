package br.gov.pr.idr.infra.iam.security;

import br.gov.pr.idr.infra.iam.user.persistence.UserJPAEntity;
import br.gov.pr.idr.infra.iam.user.persistence.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl")
class UserDetailsServiceImplTest {

    @Mock UserJPARepository userRepository;
    @Mock UserJPAEntity userEntity;
    @InjectMocks UserDetailsServiceImpl service;

    @Test
    @DisplayName("deve retornar UserDetails quando usuário é encontrado")
    void shouldReturnUserDetailsWhenFound() {
        when(userRepository.findByUsername("joao")).thenReturn(Optional.of(userEntity));

        final var result = service.loadUserByUsername("joao");

        assertNotNull(result);
        assertEquals(userEntity, result);
    }

    @Test
    @DisplayName("deve lançar UsernameNotFoundException quando usuário não é encontrado")
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("inexistente"));
    }
}
