package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.email.recuperation_code.SendEmailRecuperationCodeUseCase;
import br.gov.pr.idr.application.iam.email.reset_password.ResetPasswordUseCase;
import br.gov.pr.idr.application.iam.email.validate_code.ValidateRecuperationCodeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailController")
class EmailControllerTest {

    @Mock SendEmailRecuperationCodeUseCase sendEmailRecuperationCodeUseCase;
    @Mock ValidateRecuperationCodeUseCase validateRecuperationCodeUseCase;
    @Mock ResetPasswordUseCase resetPasswordUseCase;
    @InjectMocks EmailController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/email/send-recuperation-code deve retornar 200")
    void shouldSendRecuperationCode() throws Exception {
        doNothing().when(sendEmailRecuperationCodeUseCase).execute(any());

        mockMvc.perform(post("/v1/email/send-recuperation-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

        verify(sendEmailRecuperationCodeUseCase).execute(any());
    }

    @Test
    @DisplayName("POST /v1/email/validate-recuperation-code deve retornar 200")
    void shouldValidateRecuperationCode() throws Exception {
        doNothing().when(validateRecuperationCodeUseCase).execute(any());

        mockMvc.perform(post("/v1/email/validate-recuperation-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@test.com\",\"code\":\"ABCD1234\"}"))
                .andExpect(status().isOk());

        verify(validateRecuperationCodeUseCase).execute(any());
    }

    @Test
    @DisplayName("POST /v1/email/reset-password deve retornar 200")
    void shouldResetPassword() throws Exception {
        doNothing().when(resetPasswordUseCase).execute(any());

        mockMvc.perform(post("/v1/email/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@test.com\",\"code\":\"ABCD1234\",\"password\":\"Nova@123\",\"confirmPassword\":\"Nova@123\"}"))
                .andExpect(status().isOk());

        verify(resetPasswordUseCase).execute(any());
    }
}
