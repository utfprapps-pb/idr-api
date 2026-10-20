package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.infra.iam.security.AuthenticatedUser;
import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathOutput;
import br.gov.pr.idr.application.livestock.animal_death.create.CreateAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.animal_death.delete.DeleteAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.delete.DeleteAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.get.GetAnimalDeathByIdCommand;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.get.GetAnimalDeathByIdOutput;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.get.GetAnimalDeathByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.list.ListAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.list.ListAnimalDeathOutput;
import br.gov.pr.idr.application.livestock.animal_death.retrieve.list.ListAnimalDeathUseCase;
import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathCommand;
import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathOutput;
import br.gov.pr.idr.application.livestock.animal_death.update.UpdateAnimalDeathUseCase;
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
@DisplayName("AnimalDeathController")
class AnimalDeathControllerTest {

    @Mock CreateAnimalDeathUseCase createAnimalDeathUseCase;
    @Mock UpdateAnimalDeathUseCase updateAnimalDeathUseCase;
    @Mock DeleteAnimalDeathUseCase deleteAnimalDeathUseCase;
    @Mock GetAnimalDeathByIdUseCase getAnimalDeathByIdUseCase;
    @Mock ListAnimalDeathUseCase listAnimalDeathUseCase;
    @InjectMocks AnimalDeathController controller;

    private MockMvc mockMvc;

    private static final UsernamePasswordAuthenticationToken AUTH =
            new UsernamePasswordAuthenticationToken(
                    new AuthenticatedUser("tecnico@idr.com", PropertySearchScope::unrestricted), null, List.of());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private String basePath(final UUID propertyId, final UUID animalId) {
        return "/properties/" + propertyId + "/animals/" + animalId + "/deaths";
    }

    private String createBody(final String reason, final String deathDate) {
        return """
                {"reason":"%s","deathDate":"%s"}
                """.formatted(reason, deathDate);
    }

    @Test
    @DisplayName("POST .../animalDeath deve criar morte e retornar 200")
    void shouldCreateAnimalDeath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateAnimalDeathOutput(UUID.randomUUID(), animalId,
                "Picada de cobra", LocalDate.of(2024, 3, 10));
        when(createAnimalDeathUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("Picada de cobra", "2024-03-10")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Picada de cobra"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateAnimalDeathCommand.class);
        verify(createAnimalDeathUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
        assertEquals("Picada de cobra", captor.getValue().reason());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().deathDate());
    }

    @Test
    @DisplayName("PUT .../animalDeath/{id} deve atualizar morte e retornar 200")
    void shouldUpdateAnimalDeath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateAnimalDeathOutput(id, animalId, "Timpanismo",
                LocalDate.of(2024, 4, 1));
        when(updateAnimalDeathUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("Timpanismo", "2024-04-01")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Timpanismo"))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateAnimalDeathCommand.class);
        verify(updateAnimalDeathUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalDeathId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
    }

    @Test
    @DisplayName("DELETE .../animalDeath/{id} deve deletar morte e retornar 204")
    void shouldDeleteAnimalDeath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteAnimalDeathUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteAnimalDeathCommand.class);
        verify(deleteAnimalDeathUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalDeathId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
    }

    @Test
    @DisplayName("GET .../animalDeath/{id} deve retornar morte e 200")
    void shouldGetAnimalDeathById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetAnimalDeathByIdOutput(id, animalId, "Picada de cobra",
                LocalDate.of(2024, 3, 10));
        when(getAnimalDeathByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetAnimalDeathByIdCommand.class);
        verify(getAnimalDeathByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalDeathId());
    }

    @Test
    @DisplayName("GET .../animalDeath/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchAnimalDeathScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var animalDeathFromAnimal = new ListAnimalDeathOutput(UUID.randomUUID(), animalId,
                "Picada de cobra", LocalDate.of(2024, 3, 10));
        final var pagination = new Pagination<>(0, 10, 1L, List.of(animalDeathFromAnimal));
        when(listAnimalDeathUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListAnimalDeathCommand.class);
        verify(listAnimalDeathUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../animalDeath/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListAnimalDeathOutput>(0, 10, 0L, List.of());
        when(listAnimalDeathUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListAnimalDeathCommand.class);
        verify(listAnimalDeathUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalDeathByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (morte não pertence ao animal)")
    void shouldReturn400WhenAnimalDeathNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalDeathByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Doença não encontrada"));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
