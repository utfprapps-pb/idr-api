package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_diseases.create.CreateAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.create.CreateAnimalDiseasesOutput;
import br.gov.pr.idr.application.livestock.animal_diseases.create.CreateAnimalDiseasesUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.delete.DeleteAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.delete.DeleteAnimalDiseasesUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdOutput;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get.GetAnimalDiseasesByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list.ListAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list.ListAnimalDiseasesOutput;
import br.gov.pr.idr.application.livestock.animal_diseases.retrieve.list.ListAnimalDiseasesUseCase;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesCommand;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesOutput;
import br.gov.pr.idr.application.livestock.animal_diseases.update.UpdateAnimalDiseasesUseCase;
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
@DisplayName("AnimalDiseasesController")
class AnimalDiseasesControllerTest {

    @Mock CreateAnimalDiseasesUseCase createAnimalDiseasesUseCase;
    @Mock UpdateAnimalDiseasesUseCase updateAnimalDiseasesUseCase;
    @Mock DeleteAnimalDiseasesUseCase deleteAnimalDiseasesUseCase;
    @Mock GetAnimalDiseasesByIdUseCase getAnimalDiseasesByIdUseCase;
    @Mock ListAnimalDiseasesUseCase listAnimalDiseasesUseCase;
    @InjectMocks AnimalDiseasesController controller;

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
        return "/properties/" + propertyId + "/animals/" + animalId + "/animalDiseases";
    }

    private String createBody(final String diagnosis, final String diagnosisDate) {
        return """
                {"diagnosis":"%s","diagnosisDate":"%s"}
                """.formatted(diagnosis, diagnosisDate);
    }

    @Test
    @DisplayName("POST .../animalDiseases deve criar doença e retornar 200")
    void shouldCreateAnimalDiseases() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateAnimalDiseasesOutput(UUID.randomUUID(), animalId,
                "Febre aftosa", LocalDate.of(2024, 3, 10));
        when(createAnimalDiseasesUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("Febre aftosa", "2024-03-10")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Febre aftosa"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateAnimalDiseasesCommand.class);
        verify(createAnimalDiseasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals("Febre aftosa", captor.getValue().diagnosis());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().diagnosisDate());
    }

    @Test
    @DisplayName("PUT .../animalDiseases/{id} deve atualizar doença e retornar 200")
    void shouldUpdateAnimalDiseases() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateAnimalDiseasesOutput(id, animalId, "Brucelose",
                LocalDate.of(2024, 4, 1));
        when(updateAnimalDiseasesUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("Brucelose", "2024-04-01")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Brucelose"))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateAnimalDiseasesCommand.class);
        verify(updateAnimalDiseasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalDiseasesId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("DELETE .../animalDiseases/{id} deve deletar doença e retornar 204")
    void shouldDeleteAnimalDiseases() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteAnimalDiseasesUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteAnimalDiseasesCommand.class);
        verify(deleteAnimalDiseasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalDiseasesId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET .../animalDiseases/{id} deve retornar doença e 200")
    void shouldGetAnimalDiseasesById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetAnimalDiseasesByIdOutput(id, animalId, "Febre aftosa",
                LocalDate.of(2024, 3, 10));
        when(getAnimalDiseasesByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetAnimalDiseasesByIdCommand.class);
        verify(getAnimalDiseasesByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalDiseasesId());
    }

    @Test
    @DisplayName("GET .../animalDiseases/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchAnimalDiseasesScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var animalDiseasesFromAnimal = new ListAnimalDiseasesOutput(UUID.randomUUID(), animalId,
                "Febre aftosa", LocalDate.of(2024, 3, 10));
        final var pagination = new Pagination<>(0, 10, 1L, List.of(animalDiseasesFromAnimal));
        when(listAnimalDiseasesUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListAnimalDiseasesCommand.class);
        verify(listAnimalDiseasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../animalDiseases/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListAnimalDiseasesOutput>(0, 10, 0L, List.of());
        when(listAnimalDiseasesUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListAnimalDiseasesCommand.class);
        verify(listAnimalDiseasesUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalDiseasesByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (doença não pertence ao animal)")
    void shouldReturn400WhenAnimalDiseasesNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalDiseasesByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Doença não encontrada"));

        // NotFoundException não tem @ExceptionHandler específico em GlobalExceptionHandler; cai no handler
        // genérico de DomainException, que responde 400 (mesmo comportamento já coberto em AnimalControllerTest).
        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
