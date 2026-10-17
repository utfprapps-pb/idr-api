package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.insemination.create.CreateInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.create.CreateInseminationOutput;
import br.gov.pr.idr.application.livestock.insemination.create.CreateInseminationUseCase;
import br.gov.pr.idr.application.livestock.insemination.delete.DeleteInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.delete.DeleteInseminationUseCase;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdCommand;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdOutput;
import br.gov.pr.idr.application.livestock.insemination.retrieve.get.GetInseminationByIdUseCase;
import br.gov.pr.idr.application.livestock.insemination.retrieve.list.ListInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.retrieve.list.ListInseminationOutput;
import br.gov.pr.idr.application.livestock.insemination.retrieve.list.ListInseminationUseCase;
import br.gov.pr.idr.application.livestock.insemination.update.UpdateInseminationCommand;
import br.gov.pr.idr.application.livestock.insemination.update.UpdateInseminationOutput;
import br.gov.pr.idr.application.livestock.insemination.update.UpdateInseminationUseCase;
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
@DisplayName("InseminationController")
class InseminationControllerTest {

    @Mock CreateInseminationUseCase createInseminationUseCase;
    @Mock UpdateInseminationUseCase updateInseminationUseCase;
    @Mock DeleteInseminationUseCase deleteInseminationUseCase;
    @Mock GetInseminationByIdUseCase getInseminationByIdUseCase;
    @Mock ListInseminationUseCase listInseminationUseCase;
    @InjectMocks InseminationController controller;

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
        return "/properties/" + propertyId + "/animals/" + animalId + "/inseminations";
    }

    private String createBody(final String iaDate, final String bull) {
        return """
                {"iaDate":"%s","bull":"%s"}
                """.formatted(iaDate, bull);
    }

    @Test
    @DisplayName("POST .../inseminations deve criar inseminação e retornar 200")
    void shouldCreateInsemination() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateInseminationOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), "Touro Teste");
        when(createInseminationUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10", "Touro Teste")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bull").value("Touro Teste"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateInseminationCommand.class);
        verify(createInseminationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().iaDate());
        assertEquals("Touro Teste", captor.getValue().bull());
    }

    @Test
    @DisplayName("PUT .../inseminations/{id} deve atualizar inseminação (todos os campos) e retornar 200")
    void shouldUpdateInsemination() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateInseminationOutput(id, animalId, LocalDate.of(2024, 4, 1), "Touro Novo");
        when(updateInseminationUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-04-01", "Touro Novo")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bull").value("Touro Novo"))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateInseminationCommand.class);
        verify(updateInseminationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().inseminationId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("DELETE .../inseminations/{id} deve deletar inseminação e retornar 204")
    void shouldDeleteInsemination() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteInseminationUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteInseminationCommand.class);
        verify(deleteInseminationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().inseminationId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET .../inseminations/{id} deve retornar inseminação e 200")
    void shouldGetInseminationById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetInseminationByIdOutput(id, animalId, LocalDate.of(2024, 3, 10), "Touro Teste");
        when(getInseminationByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetInseminationByIdCommand.class);
        verify(getInseminationByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().inseminationId());
    }

    @Test
    @DisplayName("GET .../inseminations/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchInseminationsScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var inseminationFromAnimal = new ListInseminationOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), "Touro Teste");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(inseminationFromAnimal));
        when(listInseminationUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListInseminationCommand.class);
        verify(listInseminationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../inseminations/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListInseminationOutput>(0, 10, 0L, List.of());
        when(listInseminationUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListInseminationCommand.class);
        verify(listInseminationUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getInseminationByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (inseminação não pertence ao animal)")
    void shouldReturn400WhenInseminationNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getInseminationByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Inseminação não encontrada"));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
