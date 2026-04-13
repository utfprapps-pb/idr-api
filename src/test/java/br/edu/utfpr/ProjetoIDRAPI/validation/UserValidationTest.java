package br.edu.utfpr.ProjetoIDRAPI.validation;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.validation.UserValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidation userValidation;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setCpf("12345678901");
        user.setUsername("testUser");
    }

    @Test
    @DisplayName("Should return true when user is valid (CPF and Username are unique)")
    void isValid_whenUserIsValid_shouldReturnTrue() {
        when(userRepository.findByCpfAndCpfIsNotNull(user.getCpf())).thenReturn(null);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        assertDoesNotThrow(() -> userValidation.isValid(user));
    }

    @Test
    @DisplayName("Should throw exception when CPF is not unique")
    void isValid_whenCpfIsNotUnique_shouldThrowException() {
        User existingUser = new User();
        existingUser.setId(2L); // ID diferente
        existingUser.setCpf(user.getCpf());

        when(userRepository.findByCpfAndCpfIsNotNull(user.getCpf())).thenReturn(existingUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.isValid(user);
        });

        assertEquals("O CPF informado já está em uso", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when Username is not unique")
    void isValid_whenUsernameIsNotUnique_shouldThrowException() {
        User existingUser = new User();
        existingUser.setId(2L); // ID Diferente
        existingUser.setUsername(user.getUsername());

        when(userRepository.findByCpfAndCpfIsNotNull(user.getCpf())).thenReturn(null); // CPF é válido
        when(userRepository.findByUsername(user.getUsername())).thenReturn(existingUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.isValid(user);
        });

        assertEquals("O nome de usuário informado já está em uso", exception.getMessage());
    }

    @Test
    @DisplayName("Should return true when updating the same user")
    void isValid_whenUpdatingSameUser_shouldReturnTrue() {
        when(userRepository.findByCpfAndCpfIsNotNull(user.getCpf())).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        assertDoesNotThrow(() -> userValidation.isValid(user));
    }
}