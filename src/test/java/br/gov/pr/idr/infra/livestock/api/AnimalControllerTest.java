package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalOutput;
import br.gov.pr.idr.application.livestock.animal.create.CreateAnimalUseCase;
import br.gov.pr.idr.application.livestock.animal.delete.DeleteAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.delete.DeleteAnimalUseCase;
import br.gov.pr.idr.application.livestock.animal.retrieve.find.FindAnimalByIdentifierCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.find.FindAnimalByIdentifierUseCase;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdOutput;
import br.gov.pr.idr.application.livestock.animal.retrieve.get.GetAnimalByIdUseCase;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalCommand;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalOutput;
import br.gov.pr.idr.application.livestock.animal.retrieve.list.ListAnimalUseCase;
import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalOutput;
import br.gov.pr.idr.application.livestock.animal.update.UpdateAnimalUseCase;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalController")
class AnimalControllerTest {

    @Mock CreateAnimalUseCase createAnimalUseCase;
    @Mock UpdateAnimalUseCase updateAnimalUseCase;
    @Mock DeleteAnimalUseCase deleteAnimalUseCase;
    @Mock GetAnimalByIdUseCase getAnimalByIdUseCase;
    @Mock ListAnimalUseCase listAnimalUseCase;
    @Mock FindAnimalByIdentifierUseCase findAnimalByIdentifierUseCase;
    @InjectMocks AnimalController controller;

    private MockMvc mockMvc;

    private static final UsernamePasswordAuthenticationToken AUTH =
            new UsernamePasswordAuthenticationToken("tecnico@idr.com", null, List.of());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private String createBody(final UUID breedId) {
        return """
                {"breedId":"%s","type":"Bovino","identifier":"Mimosa","gender":"F",
                "bornCondition":"Normal","bornDate":"2020-01-01","bornWeight":30,
                "previousWeight":180,"currentWeight":220,"ecc":3.5}
                """.formatted(breedId);
    }

    @Test
    @DisplayName("POST /properties/{propertyId}/animals deve criar animal e retornar 200")
    void shouldCreateAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var breedId = UUID.randomUUID();
        final var output = new CreateAnimalOutput(UUID.randomUUID(), propertyId, null, breedId, "Bovino",
                "Mimosa", "F", "Normal", LocalDate.of(2020, 1, 1), 30f, 180f, 220f, 3.5f);
        when(createAnimalUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/properties/" + propertyId + "/animals")
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(breedId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value("Mimosa"))
                .andExpect(jsonPath("$.propertyId").value(propertyId.toString()));
    }

    @Test
    @DisplayName("PATCH /properties/{propertyId}/animals/{id} deve atualizar animal e retornar 200")
    void shouldUpdateAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var breedId = UUID.randomUUID();
        final var output = new UpdateAnimalOutput(id, propertyId, null, breedId, "Bovino", "Charmosa", "F",
                "Normal", LocalDate.of(2020, 1, 1), 30f, 180f, 220f, 3.5f);
        when(updateAnimalUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(patch("/properties/" + propertyId + "/animals/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody(breedId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value("Charmosa"));
    }

    @Test
    @DisplayName("DELETE /properties/{propertyId}/animals/{id} deve deletar animal e retornar 204")
    void shouldDeleteAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteAnimalUseCase).execute(any());

        mockMvc.perform(delete("/properties/" + propertyId + "/animals/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteAnimalCommand.class);
        verify(deleteAnimalUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(id, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET /properties/{propertyId}/animals/{id} deve retornar animal e 200")
    void shouldGetAnimalById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetAnimalByIdOutput(id, propertyId, null, UUID.randomUUID(), "Holandesa", "Bovino", "Mimosa",
                "F", "Normal", LocalDate.of(2020, 1, 1), 30f, 180f, 220f, 3.5f);
        when(getAnimalByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get("/properties/" + propertyId + "/animals/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.breedName").value("Holandesa"));

        final var captor = ArgumentCaptor.forClass(GetAnimalByIdCommand.class);
        verify(getAnimalByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
    }

    @Test
    @DisplayName("GET /properties/{propertyId}/animals/search escopa a busca pelo propertyId do path, " +
            "nunca retornando animais de outra propriedade (prova da correção da falha de segurança)")
    void shouldSearchAnimalsScopedByPropertyIdInPath() throws Exception {
        final var propertyA = UUID.randomUUID();
        final var animalFromPropertyA = new ListAnimalOutput(UUID.randomUUID(), propertyA, null,
                UUID.randomUUID(), "Holandesa", "Bovino", "Mimosa", "F", "Normal", LocalDate.of(2020, 1, 1), 30f, 180f,
                220f, 3.5f);
        final var pagination = new Pagination<>(0, 10, 1L, List.of(animalFromPropertyA));
        when(listAnimalUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/properties/" + propertyA + "/animals/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].propertyId").value(propertyA.toString()))
                .andExpect(jsonPath("$.items[0].breedName").value("Holandesa"));

        final var captor = ArgumentCaptor.forClass(ListAnimalCommand.class);
        verify(listAnimalUseCase).execute(captor.capture());
        // O propertyId usado na busca vem exclusivamente do path, nunca de um parâmetro de query ou do corpo,
        // o que é exatamente o que fecha a falha de segurança original (rota com wildcard ignorava a propriedade).
        assertEquals(propertyA, captor.getValue().propertyId());
    }

    @Test
    @DisplayName("GET /properties/{propertyId}/animals/search com propertyId diferente no path gera " +
            "comando escopado para a propriedade correspondente")
    void shouldScopeSearchToPathPropertyIdForDifferentProperty() throws Exception {
        final var propertyB = UUID.randomUUID();
        final var pagination = new Pagination<ListAnimalOutput>(0, 10, 0L, List.of());
        when(listAnimalUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/properties/" + propertyB + "/animals/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListAnimalCommand.class);
        verify(listAnimalUseCase).execute(captor.capture());
        assertEquals(propertyB, captor.getValue().propertyId());
    }

    @Test
    @DisplayName("GET /properties/{propertyId}/animals/findAnimal/{identifier} deve retornar animal e 200")
    void shouldFindAnimalByIdentifier() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var output = new GetAnimalByIdOutput(UUID.randomUUID(), propertyId, null, UUID.randomUUID(),
                "Holandesa", "Bovino", "Mimosa", "F", "Normal", LocalDate.of(2020, 1, 1), 30f, 180f, 220f, 3.5f);
        when(findAnimalByIdentifierUseCase.execute(any())).thenReturn(Optional.of(output));

        mockMvc.perform(get("/properties/" + propertyId + "/animals/findAnimal/Mimosa").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value("Mimosa"));

        final var captor = ArgumentCaptor.forClass(FindAnimalByIdentifierCommand.class);
        verify(findAnimalByIdentifierUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals("Mimosa", captor.getValue().identifier());
    }

    @Test
    @DisplayName("GET /properties/{propertyId}/animals/findAnimal/{identifier} deve retornar 404 quando não encontrado")
    void shouldReturn404WhenIdentifierNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        when(findAnimalByIdentifierUseCase.execute(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/properties/" + propertyId + "/animals/findAnimal/Inexistente").principal(AUTH))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get("/properties/" + propertyId + "/animals/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (animal não pertence à propriedade)")
    void shouldReturn400WhenAnimalNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalByIdUseCase.execute(any())).thenThrow(NotFoundException.with("Animal não encontrado"));

        // NotFoundException não tem @ExceptionHandler específico em GlobalExceptionHandler; cai no handler
        // genérico de DomainException, que responde 400 (comportamento já existente e coberto por
        // GlobalExceptionHandlerTest).
        mockMvc.perform(get("/properties/" + propertyId + "/animals/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
