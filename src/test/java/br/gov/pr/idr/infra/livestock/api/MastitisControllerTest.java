package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisOutput;
import br.gov.pr.idr.application.livestock.mastitis.create.CreateMastitisUseCase;
import br.gov.pr.idr.application.livestock.mastitis.delete.DeleteMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.delete.DeleteMastitisUseCase;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdCommand;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdOutput;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.get.GetMastitisByIdUseCase;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.list.ListMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.list.ListMastitisOutput;
import br.gov.pr.idr.application.livestock.mastitis.retrieve.list.ListMastitisUseCase;
import br.gov.pr.idr.application.livestock.mastitis.update.UpdateMastitisCommand;
import br.gov.pr.idr.application.livestock.mastitis.update.UpdateMastitisOutput;
import br.gov.pr.idr.application.livestock.mastitis.update.UpdateMastitisUseCase;
import br.gov.pr.idr.domain.shared.tactical.exceptions.ForbiddenException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.infra.shared.error.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MastitisController")
class MastitisControllerTest {

    @Mock CreateMastitisUseCase createMastitisUseCase;
    @Mock UpdateMastitisUseCase updateMastitisUseCase;
    @Mock DeleteMastitisUseCase deleteMastitisUseCase;
    @Mock GetMastitisByIdUseCase getMastitisByIdUseCase;
    @Mock ListMastitisUseCase listMastitisUseCase;
    @InjectMocks MastitisController controller;

    private MockMvc mockMvc;

    private static final UsernamePasswordAuthenticationToken AUTH =
            new UsernamePasswordAuthenticationToken("tecnico@idr.com", null, List.of());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private String basePath(final UUID propertyId, final UUID animalId) {
        return "/properties/" + propertyId + "/animals/" + animalId + "/mastitis";
    }

    private String createBody(final String diagnoseDate, final String mastitisType) {
        return """
                {"diagnoseDate":"%s","mastitisType":"%s","cmtResult":"+","ad":"Positivo","ae":"Negativo",
                "pd":"Positivo","pe":"Negativo"}
                """.formatted(diagnoseDate, mastitisType);
    }

    @Test
    @DisplayName("POST .../mastitis deve criar mastite e retornar 200")
    void shouldCreateMastitis() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateMastitisOutput(UUID.randomUUID(), animalId, LocalDate.of(2024, 3, 10),
                "Clinica", "+", "Positivo", "Negativo", "Positivo", "Negativo");
        when(createMastitisUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10", "Clinica")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mastitisType").value("Clinica"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateMastitisCommand.class);
        verify(createMastitisUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().diagnoseDate());
        assertEquals("Clinica", captor.getValue().mastitisType());
    }

    @Test
    @DisplayName("PUT .../mastitis/{id} deve atualizar mastite (todos os campos) e retornar 200")
    void shouldUpdateMastitis() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateMastitisOutput(id, animalId, LocalDate.of(2024, 4, 1), "Subclinica", "-",
                "Negativo", "Positivo", "Negativo", "Positivo");
        when(updateMastitisUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-04-01", "Subclinica")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mastitisType").value("Subclinica"))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateMastitisCommand.class);
        verify(updateMastitisUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().mastitisId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("DELETE .../mastitis/{id} deve deletar mastite e retornar 204")
    void shouldDeleteMastitis() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteMastitisUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteMastitisCommand.class);
        verify(deleteMastitisUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().mastitisId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET .../mastitis/{id} deve retornar mastite e 200")
    void shouldGetMastitisById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetMastitisByIdOutput(id, animalId, LocalDate.of(2024, 3, 10), "Clinica", "+",
                "Positivo", "Negativo", "Positivo", "Negativo");
        when(getMastitisByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetMastitisByIdCommand.class);
        verify(getMastitisByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().mastitisId());
    }

    @Test
    @DisplayName("GET .../mastitis/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchMastitisScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var mastitisFromAnimal = new ListMastitisOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), "Clinica", "+", "Positivo", "Negativo", "Positivo", "Negativo");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(mastitisFromAnimal));
        when(listMastitisUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListMastitisCommand.class);
        verify(listMastitisUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../mastitis/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListMastitisOutput>(0, 10, 0L, List.of());
        when(listMastitisUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListMastitisCommand.class);
        verify(listMastitisUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getMastitisByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (mastite não pertence ao animal)")
    void shouldReturn400WhenMastitisNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getMastitisByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Mastite não encontrada"));

        // NotFoundException não tem @ExceptionHandler específico em GlobalExceptionHandler; cai no handler
        // genérico de DomainException, que responde 400 (mesmo comportamento já coberto em AnimalControllerTest).
        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
