package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseOutput;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.create.CreatePregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete.DeletePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete.DeletePregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdOutput;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get.GetPregnancyDiagnoseByIdUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list.ListPregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list.ListPregnancyDiagnoseOutput;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list.ListPregnancyDiagnoseUseCase;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.update.UpdatePregnancyDiagnoseCommand;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.update.UpdatePregnancyDiagnoseOutput;
import br.gov.pr.idr.application.livestock.pregnancy_diagnose.update.UpdatePregnancyDiagnoseUseCase;
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
@DisplayName("PregnancyDiagnoseController")
class PregnancyDiagnoseControllerTest {

    @Mock CreatePregnancyDiagnoseUseCase createPregnancyDiagnoseUseCase;
    @Mock UpdatePregnancyDiagnoseUseCase updatePregnancyDiagnoseUseCase;
    @Mock DeletePregnancyDiagnoseUseCase deletePregnancyDiagnoseUseCase;
    @Mock GetPregnancyDiagnoseByIdUseCase getPregnancyDiagnoseByIdUseCase;
    @Mock ListPregnancyDiagnoseUseCase listPregnancyDiagnoseUseCase;
    @InjectMocks PregnancyDiagnoseController controller;

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
        return "/properties/" + propertyId + "/animals/" + animalId + "/pregnancy-diagnoses";
    }

    private String createBody(final String diagnoseDate, final String lastIa) {
        return """
                {"diagnoseDate":"%s","lastIa":"%s"}
                """.formatted(diagnoseDate, lastIa);
    }

    @Test
    @DisplayName("POST .../pregnancyDiagnose deve criar diagnóstico de gestação e retornar 200")
    void shouldCreatePregnancyDiagnose() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreatePregnancyDiagnoseOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), LocalDate.of(2024, 1, 10));
        when(createPregnancyDiagnoseUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10", "2024-01-10")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastIa").value("2024-01-10"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreatePregnancyDiagnoseCommand.class);
        verify(createPregnancyDiagnoseUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().diagnoseDate());
        assertEquals(LocalDate.of(2024, 1, 10), captor.getValue().lastIa());
    }

    @Test
    @DisplayName("PUT .../pregnancyDiagnose/{id} deve atualizar diagnóstico de gestação e retornar 200")
    void shouldUpdatePregnancyDiagnose() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdatePregnancyDiagnoseOutput(id, animalId, LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 2, 1));
        when(updatePregnancyDiagnoseUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-04-01", "2024-02-01")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastIa").value("2024-02-01"))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdatePregnancyDiagnoseCommand.class);
        verify(updatePregnancyDiagnoseUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().pregnancyDiagnoseId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("DELETE .../pregnancyDiagnose/{id} deve deletar diagnóstico de gestação e retornar 204")
    void shouldDeletePregnancyDiagnose() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deletePregnancyDiagnoseUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeletePregnancyDiagnoseCommand.class);
        verify(deletePregnancyDiagnoseUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().pregnancyDiagnoseId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET .../pregnancyDiagnose/{id} deve retornar diagnóstico de gestação e 200")
    void shouldGetPregnancyDiagnoseById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetPregnancyDiagnoseByIdOutput(id, animalId, LocalDate.of(2024, 3, 10),
                LocalDate.of(2024, 1, 10));
        when(getPregnancyDiagnoseByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetPregnancyDiagnoseByIdCommand.class);
        verify(getPregnancyDiagnoseByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().pregnancyDiagnoseId());
    }

    @Test
    @DisplayName("GET .../pregnancyDiagnose/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchPregnancyDiagnoseScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var pregnancyDiagnoseFromAnimal = new ListPregnancyDiagnoseOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), LocalDate.of(2024, 1, 10));
        final var pagination = new Pagination<>(0, 10, 1L, List.of(pregnancyDiagnoseFromAnimal));
        when(listPregnancyDiagnoseUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListPregnancyDiagnoseCommand.class);
        verify(listPregnancyDiagnoseUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../pregnancyDiagnose/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListPregnancyDiagnoseOutput>(0, 10, 0L, List.of());
        when(listPregnancyDiagnoseUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListPregnancyDiagnoseCommand.class);
        verify(listPregnancyDiagnoseUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getPregnancyDiagnoseByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (diagnóstico não pertence ao animal)")
    void shouldReturn400WhenPregnancyDiagnoseNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getPregnancyDiagnoseByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Diagnóstico de gestação não encontrado"));

        // NotFoundException não tem @ExceptionHandler específico em GlobalExceptionHandler; cai no handler
        // genérico de DomainException, que responde 400 (mesmo comportamento já coberto em AnimalControllerTest).
        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
