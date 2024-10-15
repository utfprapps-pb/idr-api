package br.edu.utfpr.ProjetoIDRAPI.validation;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.validation.UserValidator;
import br.edu.utfpr.ProjetoIDRAPI.utils.BaseUser;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserRepository repository;

    @Spy
    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private ConstraintValidatorContext context;

    private BaseUser user;

    @BeforeEach
    void setUp() {
        user = mock(BaseUser.class);
        lenient().when(user.getId()).thenReturn(1L);
        lenient().when(user.getCpf()).thenReturn("12345678901");
        lenient().when(user.getUsername()).thenReturn("testUser");
        lenient().doNothing().when(userValidator).handleMessage(any(), any(), any());
    }

    @Test
    @DisplayName("Should return true when user is valid")
    void testIsValid_withValidUser() {
        when(repository.findByCpfAndCpfIsNotNull("12345678901")).thenReturn(null);
        when(repository.findByUsername("testUser")).thenReturn(null);

        assertTrue(userValidator.isValid(user, context));
    }

    @Test
    @DisplayName("Should return false when CPF is not unique")
    void testIsValid_withNonUniqueCPF() {
        User existingUser = User.builder()
                .id(2L)
                .cpf("12345678901")
                .build();

        when(repository.findByCpfAndCpfIsNotNull("12345678901")).thenReturn(existingUser);

        assertFalse(userValidator.isValid(user, context));
    }

    @Test
    @DisplayName("Should return false when username is not unique")
    void testIsValid_withNonUniqueUsername() {
        User existingUser = User.builder()
                .id(2L)
                .username("testUser")
                .build();

        when(repository.findByUsername("testUser")).thenReturn(existingUser);

        assertFalse(userValidator.isValid(user, context));
    }

    @Test
    @DisplayName("Should return true when CPF and username match the current user")
    void testIsValid_withMatchingCPFAndUsername() {
        when(repository.findByCpfAndCpfIsNotNull("12345678901")).thenReturn(null);
        when(repository.findByUsername("testUser")).thenReturn(null);

        assertTrue(userValidator.isValid(user, context));
    }
}