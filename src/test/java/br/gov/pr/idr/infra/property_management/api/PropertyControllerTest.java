package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyOutput;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyUseCase;
import br.gov.pr.idr.application.property_management.property.delete.DeletePropertyUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.attachment.GetPropertyAttachmentOutput;
import br.gov.pr.idr.application.property_management.property.retrieve.attachment.GetPropertyAttachmentUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.get.GetPropertyByIdUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyOutput;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyUseCase;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyOutput;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyUseCase;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.infra.property_management.property.models.create.CreatePropertyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PropertyController")
class PropertyControllerTest {

    @Mock CreatePropertyUseCase createPropertyUseCase;
    @Mock UpdatePropertyUseCase updatePropertyUseCase;
    @Mock GetPropertyByIdUseCase getPropertyByIdUseCase;
    @Mock SearchPropertyUseCase searchPropertyUseCase;
    @Mock DeletePropertyUseCase deletePropertyUseCase;
    @Mock GetPropertyAttachmentUseCase getPropertyAttachmentUseCase;
    @InjectMocks PropertyController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private MockMultipartFile propertyPart(final UUID producerId, final UUID cityId, final String name) {
        return new MockMultipartFile("property", "", MediaType.APPLICATION_JSON_VALUE,
                ("{\"name\":\"" + name + "\",\"latitude\":\"-25.4\",\"longitude\":\"-49.2\"," +
                        "\"producerId\":\"" + producerId + "\",\"cityId\":\"" + cityId + "\"}").getBytes());
    }

    private MockMultipartFile propertyPartWithCollaborators(final UUID producerId, final UUID cityId,
                                                              final String name) {
        return new MockMultipartFile("property", "", MediaType.APPLICATION_JSON_VALUE,
                ("{\"name\":\"" + name + "\",\"latitude\":\"-25.4\",\"longitude\":\"-49.2\"," +
                        "\"producerId\":\"" + producerId + "\",\"cityId\":\"" + cityId + "\"," +
                        "\"collaborators\":[{\"name\":\"Maria\",\"hoursPerDay\":\"8\"}]}").getBytes());
    }

    @Test
    @DisplayName("POST /v1/properties deve criar propriedade e retornar 200")
    void shouldCreateProperty() throws Exception {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var attachment = new CreatePropertyOutput.AttachmentSummary(UUID.randomUUID(), "contrato.pdf",
                                                                           "application/pdf", 1L);
        final var output = new CreatePropertyOutput(UUID.randomUUID(), "Fazenda Boa Vista", List.of(attachment));
        when(createPropertyUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(multipart("/v1/properties")
                        .file(propertyPart(producerId, cityId, "Fazenda Boa Vista")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fazenda Boa Vista"));
    }

    @Test
    @DisplayName("POST /v1/properties com anexos deve repassar os arquivos para o use case")
    void shouldCreatePropertyWithAttachments() throws Exception {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new CreatePropertyOutput(UUID.randomUUID(), "Fazenda Boa Vista", List.of());
        when(createPropertyUseCase.execute(any())).thenReturn(output);

        final var attachment = new MockMultipartFile("attachments", "contrato.pdf",
                MediaType.APPLICATION_PDF_VALUE, "conteudo".getBytes());

        mockMvc.perform(multipart("/v1/properties")
                        .file(propertyPart(producerId, cityId, "Fazenda Boa Vista"))
                        .file(attachment))
                .andExpect(status().isOk());

        verify(createPropertyUseCase).execute(any());
    }

    @Test
    @DisplayName("PUT /v1/properties/{id} deve atualizar propriedade e retornar 200")
    void shouldUpdateProperty() throws Exception {
        final var id = UUID.randomUUID();
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new UpdatePropertyOutput(id, "Fazenda Atualizada", List.of());
        when(updatePropertyUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(multipart(HttpMethod.PUT, "/v1/properties/{id}", id)
                        .file(propertyPart(producerId, cityId, "Fazenda Atualizada")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fazenda Atualizada"));
    }

    @Test
    @DisplayName("GET /v1/properties/{id} deve retornar propriedade por ID")
    void shouldGetPropertyById() throws Exception {
        final var id = UUID.randomUUID();
        final var producer = new GetPropertyQueryResult.Producer(UUID.randomUUID(), "João");
        final var city = new GetPropertyQueryResult.City(UUID.randomUUID(), "Curitiba");
        final var result = new GetPropertyQueryResult(id, "Fazenda", BigDecimal.TEN, BigDecimal.ONE,
                0.0, 0.0, 0.0, 0.0, BigDecimal.valueOf(-25.4), BigDecimal.valueOf(-49.2),
                null, null, producer, city, List.of(), List.of(), List.of());
        when(getPropertyByIdUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(get("/v1/properties/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fazenda"));
    }

    @Test
    @DisplayName("GET /v1/properties/{id}/attachments/{attachmentId} deve retornar o binário do anexo")
    void shouldDownloadAttachment() throws Exception {
        final var id = UUID.randomUUID();
        final var attachmentId = UUID.randomUUID();
        final var output = new GetPropertyAttachmentOutput("contrato.pdf", "application/pdf", "conteudo".getBytes());
        when(getPropertyAttachmentUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get("/v1/properties/" + id + "/attachments/" + attachmentId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"contrato.pdf\""));
    }

    @Test
    @DisplayName("GET /v1/properties/search deve retornar paginação de propriedades")
    void shouldSearchProperties() throws Exception {
        final var output = new SearchPropertyOutput(UUID.randomUUID(), "Fazenda",
                BigDecimal.TEN, BigDecimal.ONE, UUID.randomUUID(), UUID.randomUUID());
        final var pagination = new Pagination<>(0, 10, 1L, List.of(output));
        when(searchPropertyUseCase.execute(any())).thenReturn(pagination);
        final var auth = new UsernamePasswordAuthenticationToken("tecnico@idr.com", null, List.of());

        mockMvc.perform(get("/v1/properties/search").principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @DisplayName("GET /v1/properties/search sem Authentication deve lançar IllegalStateException")
    void shouldThrowWhenSearchAuthenticationIsNull() {
        assertThrows(ServletException.class, () -> mockMvc.perform(get("/v1/properties/search")));
    }

    @Test
    @DisplayName("GET /v1/properties/search com Authentication sem principal deve lançar IllegalStateException")
    void shouldThrowWhenSearchAuthenticationPrincipalIsNull() {
        final var authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);

        assertThrows(ServletException.class,
                () -> mockMvc.perform(get("/v1/properties/search").principal(authentication)));
    }

    @Test
    @DisplayName("DELETE /v1/properties/{id} deve deletar propriedade e retornar 204")
    void shouldDeleteProperty() throws Exception {
        doNothing().when(deletePropertyUseCase).execute(any());

        mockMvc.perform(delete("/v1/properties/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /v1/properties com colaboradores deve mapear a lista de colaboradores")
    void shouldCreatePropertyWithCollaborators() throws Exception {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new CreatePropertyOutput(UUID.randomUUID(), "Fazenda Boa Vista", List.of());
        when(createPropertyUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(multipart("/v1/properties")
                        .file(propertyPartWithCollaborators(producerId, cityId, "Fazenda Boa Vista")))
                .andExpect(status().isOk());

        verify(createPropertyUseCase).execute(any());
    }

    @Test
    @DisplayName("PUT /v1/properties/{id} com colaboradores deve mapear a lista de colaboradores")
    void shouldUpdatePropertyWithCollaborators() throws Exception {
        final var id = UUID.randomUUID();
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new UpdatePropertyOutput(id, "Fazenda Atualizada", List.of());
        when(updatePropertyUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(multipart(HttpMethod.PUT, "/v1/properties/{id}", id)
                        .file(propertyPartWithCollaborators(producerId, cityId, "Fazenda Atualizada")))
                .andExpect(status().isOk());

        verify(updatePropertyUseCase).execute(any());
    }

    @Test
    @DisplayName("PUT /v1/properties/{id} com anexos deve repassar os arquivos para o use case")
    void shouldUpdatePropertyWithAttachments() throws Exception {
        final var id = UUID.randomUUID();
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new UpdatePropertyOutput(id, "Fazenda Atualizada", List.of());
        when(updatePropertyUseCase.execute(any())).thenReturn(output);

        final var attachment = new MockMultipartFile("attachments", "contrato.pdf",
                MediaType.APPLICATION_PDF_VALUE, "conteudo".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT, "/v1/properties/{id}", id)
                        .file(propertyPart(producerId, cityId, "Fazenda Atualizada"))
                        .file(attachment))
                .andExpect(status().isOk());

        verify(updatePropertyUseCase).execute(any());
    }

    @Test
    @DisplayName("readBytes() deve encapsular IOException em UncheckedIOException")
    void shouldWrapIOExceptionWhenReadingAttachmentBytes() throws Exception {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var request = new CreatePropertyRequest("Fazenda Boa Vista", BigDecimal.valueOf(-25.4),
                BigDecimal.valueOf(-49.2), null, null, null, null, null, null,
                producerId, cityId, List.of(), null);

        final var failingAttachment = mock(MultipartFile.class);
        when(failingAttachment.getOriginalFilename()).thenReturn("contrato.pdf");
        when(failingAttachment.getBytes()).thenThrow(new IOException("boom"));

        assertThrows(UncheckedIOException.class,
                () -> controller.create(request, List.of(failingAttachment)));
    }
}
