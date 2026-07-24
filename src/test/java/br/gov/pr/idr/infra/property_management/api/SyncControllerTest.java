package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameOutput;
import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameUseCase;
import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncOutput;
import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncUseCase;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncUseCase;
import br.gov.pr.idr.domain.property_management.sync.entity.SyncEntityResult;
import br.gov.pr.idr.domain.property_management.sync.vo.SyncEntityStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.ServletException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SyncController")
class SyncControllerTest {

    @Mock DownloadSyncUseCase downloadSyncUseCase;
    @Mock UploadSyncUseCase uploadSyncUseCase;
    @Mock FindUserByUsernameUseCase findUserByUsernameUseCase;
    @InjectMocks SyncController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /v1/sync/download com TECNICO autenticado deve retornar 200")
    void shouldReturnDownloadPayload() throws Exception {
        final var userId = UUID.randomUUID();
        final var output = new DownloadSyncOutput("1.0", Instant.now(), Map.of());

        when(findUserByUsernameUseCase.execute(any())).thenReturn(new FindUserByUsernameOutput(userId, "Técnico"));
        when(downloadSyncUseCase.execute(any())).thenReturn(output);

        final var auth = new UsernamePasswordAuthenticationToken("tecnico@idr.com", null, List.of());

        mockMvc.perform(get("/v1/sync/download").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schemaVersion").value("1.0"))
                .andExpect(jsonPath("$.regions").isArray())
                .andExpect(jsonPath("$.cities").isArray())
                .andExpect(jsonPath("$.producers").isArray());
    }

    @Test
    @DisplayName("POST /v1/sync/upload com payload válido deve retornar 200 com resultados")
    void shouldReturnUploadResults() throws Exception {
        final var localId = UUID.randomUUID();
        final var serverId = UUID.randomUUID();

        when(findUserByUsernameUseCase.execute(any()))
                .thenReturn(new FindUserByUsernameOutput(UUID.randomUUID(), "Técnico"));
        when(uploadSyncUseCase.execute(any())).thenReturn(
                List.of(new SyncEntityResult(localId, serverId, SyncEntityStatus.CREATED, null)));

        final var auth = new UsernamePasswordAuthenticationToken("tecnico@idr.com", null, List.of());
        final var body = """
                {"entities":[{"type":"PRODUCER","localId":"%s","data":{"name":"João","cpf":"529.982.247-25"}}]}
                """.formatted(localId);

        mockMvc.perform(post("/v1/sync/upload")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].status").value("CREATED"));
    }

    @Test
    @DisplayName("GET /v1/sync/download sem Authentication deve lançar IllegalStateException")
    void shouldThrowWhenAuthenticationIsNull() {
        assertThrows(ServletException.class, () -> mockMvc.perform(get("/v1/sync/download")));
    }

    @Test
    @DisplayName("GET /v1/sync/download com Authentication sem principal deve lançar IllegalStateException")
    void shouldThrowWhenAuthenticationPrincipalIsNull() {
        final var authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);

        assertThrows(ServletException.class,
                () -> mockMvc.perform(get("/v1/sync/download").principal(authentication)));
    }
}
