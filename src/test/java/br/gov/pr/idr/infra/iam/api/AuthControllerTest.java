package br.gov.pr.idr.infra.iam.api;

import br.gov.pr.idr.application.iam.refresh_token.rotate.RotateRefreshTokenOutput;
import br.gov.pr.idr.application.iam.refresh_token.rotate.RotateRefreshTokenUseCase;
import br.gov.pr.idr.infra.iam.security.config.JwtProperties;
import br.gov.pr.idr.infra.iam.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController")
class AuthControllerTest {

    @Mock RotateRefreshTokenUseCase rotateRefreshTokenUseCase;
    @Mock JwtService jwtService;
    @Mock JwtProperties jwtProperties;
    @Mock UserDetailsService userDetailsService;
    @InjectMocks AuthController controller;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/auth/refresh deve retornar 200 com novo access token e refresh token")
    void shouldRefreshToken() throws Exception {
        final var output = new RotateRefreshTokenOutput("joao.silva", "new-refresh-token");
        final var userDetails = mock(UserDetails.class);
        when(jwtProperties.refreshTokenExpirationDays()).thenReturn(7L);
        when(rotateRefreshTokenUseCase.execute(any())).thenReturn(output);
        when(userDetailsService.loadUserByUsername("joao.silva")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");

        mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"old-refresh-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }
}
