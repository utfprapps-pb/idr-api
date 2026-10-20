package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.infra.iam.security.AuthenticatedUser;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import java.math.BigDecimal;
import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthOutput;
import br.gov.pr.idr.application.livestock.animal_childbirth.create.CreateAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.delete.DeleteAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.delete.DeleteAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdOutput;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get.GetAnimalChildbirthByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthOutput;
import br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list.ListAnimalChildbirthUseCase;
import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthCommand;
import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthOutput;
import br.gov.pr.idr.application.livestock.animal_childbirth.update.UpdateAnimalChildbirthUseCase;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalChildbirthController")
class AnimalChildbirthControllerTest {

    @Mock CreateAnimalChildbirthUseCase createAnimalChildbirthUseCase;
    @Mock UpdateAnimalChildbirthUseCase updateAnimalChildbirthUseCase;
    @Mock DeleteAnimalChildbirthUseCase deleteAnimalChildbirthUseCase;
    @Mock GetAnimalChildbirthByIdUseCase getAnimalChildbirthByIdUseCase;
    @Mock ListAnimalChildbirthUseCase listAnimalChildbirthUseCase;
    @InjectMocks AnimalChildbirthController controller;

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
        return "/properties/" + propertyId + "/animals/" + animalId + "/childbirths";
    }

    private String createBody(final String childbirthDate) {
        return """
                {"childbirthDate":"%s","gender":"FEMALE","weight":35.5,"condition":"ALIVE",
                 "breedId":"6f8b8953-72ab-4525-afdf-2ee4d0561b5e","calfId":null}
                """.formatted(childbirthDate);
    }

    @Test
    @DisplayName("POST .../animalChildbirth deve criar parto e retornar 200")
    void shouldCreateAnimalChildbirth() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateAnimalChildbirthOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE, UUID.fromString("6f8b8953-72ab-4525-afdf-2ee4d0561b5e"), null);
        when(createAnimalChildbirthUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gender").value("FEMALE"))
                .andExpect(jsonPath("$.condition").value("ALIVE"))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateAnimalChildbirthCommand.class);
        verify(createAnimalChildbirthUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().childbirthDate());
        assertEquals(ChildbirthGender.FEMALE, captor.getValue().gender());
        assertEquals(new BigDecimal("35.5"), captor.getValue().weight());
        assertEquals(ChildbirthCondition.ALIVE, captor.getValue().condition());
        assertEquals(UUID.fromString("6f8b8953-72ab-4525-afdf-2ee4d0561b5e"), captor.getValue().breedId());
        assertNull(captor.getValue().calfId());
    }

    @Test
    @DisplayName("PUT .../animalChildbirth/{id} deve atualizar parto e retornar 200")
    void shouldUpdateAnimalChildbirth() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateAnimalChildbirthOutput(id, animalId,
                LocalDate.of(2024, 4, 1), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE, UUID.fromString("6f8b8953-72ab-4525-afdf-2ee4d0561b5e"), null);
        when(updateAnimalChildbirthUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-04-01")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(35.5))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateAnimalChildbirthCommand.class);
        verify(updateAnimalChildbirthUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalChildbirthId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
    }

    @Test
    @DisplayName("DELETE .../animalChildbirth/{id} deve deletar parto e retornar 204")
    void shouldDeleteAnimalChildbirth() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteAnimalChildbirthUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteAnimalChildbirthCommand.class);
        verify(deleteAnimalChildbirthUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalChildbirthId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
    }

    @Test
    @DisplayName("GET .../animalChildbirth/{id} deve retornar parto e 200")
    void shouldGetAnimalChildbirthById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetAnimalChildbirthByIdOutput(id, animalId,
                LocalDate.of(2024, 3, 10), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE, UUID.fromString("6f8b8953-72ab-4525-afdf-2ee4d0561b5e"), null, "Holandês", null);
        when(getAnimalChildbirthByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.breedName").value("Holandês"));

        final var captor = ArgumentCaptor.forClass(GetAnimalChildbirthByIdCommand.class);
        verify(getAnimalChildbirthByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalChildbirthId());
    }

    @Test
    @DisplayName("GET .../animalChildbirth/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchAnimalChildbirthScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var animalChildbirthFromAnimal = new ListAnimalChildbirthOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), ChildbirthGender.FEMALE, new BigDecimal("35.5"), ChildbirthCondition.ALIVE, UUID.fromString("6f8b8953-72ab-4525-afdf-2ee4d0561b5e"), null, "Holandês", null);
        final var pagination = new Pagination<>(0, 10, 1L, List.of(animalChildbirthFromAnimal));
        when(listAnimalChildbirthUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListAnimalChildbirthCommand.class);
        verify(listAnimalChildbirthUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../animalChildbirth/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListAnimalChildbirthOutput>(0, 10, 0L, List.of());
        when(listAnimalChildbirthUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListAnimalChildbirthCommand.class);
        verify(listAnimalChildbirthUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalChildbirthByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (parto não pertence ao animal)")
    void shouldReturn400WhenAnimalChildbirthNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalChildbirthByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Doença não encontrada"));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
