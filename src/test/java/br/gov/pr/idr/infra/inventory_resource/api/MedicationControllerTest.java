package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationOutput;
import br.gov.pr.idr.application.inventory_resource.medication.create.CreateMedicationUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.delete.DeleteMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.delete.DeleteMedicationUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdCommand;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdOutput;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.get.GetMedicationByIdUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.list.ListMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.list.ListMedicationOutput;
import br.gov.pr.idr.application.inventory_resource.medication.retrieve.list.ListMedicationUseCase;
import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationCommand;
import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationOutput;
import br.gov.pr.idr.application.inventory_resource.medication.update.UpdateMedicationUseCase;
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
@DisplayName("MedicationController")
class MedicationControllerTest {

    @Mock CreateMedicationUseCase createMedicationUseCase;
    @Mock UpdateMedicationUseCase updateMedicationUseCase;
    @Mock DeleteMedicationUseCase deleteMedicationUseCase;
    @Mock GetMedicationByIdUseCase getMedicationByIdUseCase;
    @Mock ListMedicationUseCase listMedicationUseCase;
    @InjectMocks MedicationController controller;

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
        return "/properties/" + propertyId + "/animals/" + animalId + "/medications";
    }

    private String createBody(final UUID productId, final String applicationDate, final String appliedDose,
                               final String applicationWay) {
        return """
                {"productId":"%s","applicationDate":"%s","appliedDose":"%s","applicationWay":"%s"}
                """.formatted(productId, applicationDate, appliedDose, applicationWay);
    }

    @Test
    @DisplayName("POST .../medications deve criar medicação e retornar 200")
    void shouldCreateMedication() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var productId = UUID.randomUUID();
        final var output = new CreateMedicationOutput(UUID.randomUUID(), animalId, productId,
                LocalDate.of(2024, 3, 10), "10ml", "Intramuscular");
        when(createMedicationUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(productId, "2024-03-10", "10ml", "Intramuscular")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appliedDose").value("10ml"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateMedicationCommand.class);
        verify(createMedicationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals(productId, captor.getValue().productId());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().applicationDate());
        assertEquals("10ml", captor.getValue().appliedDose());
        assertEquals("Intramuscular", captor.getValue().applicationWay());
    }

    @Test
    @DisplayName("PUT .../medications/{id} deve atualizar medicação (todos os campos) e retornar 200")
    void shouldUpdateMedication() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var productId = UUID.randomUUID();
        final var output = new UpdateMedicationOutput(id, animalId, productId, LocalDate.of(2024, 4, 1),
                "20ml", "Oral");
        when(updateMedicationUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(productId, "2024-04-01", "20ml", "Oral")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appliedDose").value("20ml"))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateMedicationCommand.class);
        verify(updateMedicationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().medicationId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals(productId, captor.getValue().productId());
    }

    @Test
    @DisplayName("DELETE .../medications/{id} deve deletar medicação e retornar 204")
    void shouldDeleteMedication() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteMedicationUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteMedicationCommand.class);
        verify(deleteMedicationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().medicationId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET .../medications/{id} deve retornar medicação e 200")
    void shouldGetMedicationById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var productId = UUID.randomUUID();
        final var output = new GetMedicationByIdOutput(id, animalId, productId, LocalDate.of(2024, 3, 10),
                "10ml", "Intramuscular");
        when(getMedicationByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetMedicationByIdCommand.class);
        verify(getMedicationByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().medicationId());
    }

    @Test
    @DisplayName("GET .../medications/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchMedicationsScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var productId = UUID.randomUUID();
        final var medicationFromAnimal = new ListMedicationOutput(UUID.randomUUID(), animalId, productId,
                LocalDate.of(2024, 3, 10), "10ml", "Intramuscular");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(medicationFromAnimal));
        when(listMedicationUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListMedicationCommand.class);
        verify(listMedicationUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../medications/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListMedicationOutput>(0, 10, 0L, List.of());
        when(listMedicationUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListMedicationCommand.class);
        verify(listMedicationUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getMedicationByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (medicação não pertence ao animal)")
    void shouldReturn400WhenMedicationNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getMedicationByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Medicação não encontrada"));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
