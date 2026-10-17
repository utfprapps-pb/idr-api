package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animal_purchases.create.CreateAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.create.CreateAnimalPurchasesOutput;
import br.gov.pr.idr.application.livestock.animal_purchases.create.CreateAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.delete.DeleteAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.delete.DeleteAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get.GetAnimalPurchasesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get.GetAnimalPurchasesByIdOutput;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.get.GetAnimalPurchasesByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesOutput;
import br.gov.pr.idr.application.livestock.animal_purchases.retrieve.list.ListAnimalPurchasesUseCase;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesCommand;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesOutput;
import br.gov.pr.idr.application.livestock.animal_purchases.update.UpdateAnimalPurchasesUseCase;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalPurchasesController")
class AnimalPurchasesControllerTest {

    @Mock CreateAnimalPurchasesUseCase createAnimalPurchasesUseCase;
    @Mock UpdateAnimalPurchasesUseCase updateAnimalPurchasesUseCase;
    @Mock DeleteAnimalPurchasesUseCase deleteAnimalPurchasesUseCase;
    @Mock GetAnimalPurchasesByIdUseCase getAnimalPurchasesByIdUseCase;
    @Mock ListAnimalPurchasesUseCase listAnimalPurchasesUseCase;
    @InjectMocks AnimalPurchasesController controller;

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
        return "/properties/" + propertyId + "/animals/" + animalId + "/purchases";
    }

    private String createBody(final String datePurchase, final String birthDate, final String value) {
        return """
                {"datePurchase":"%s","birthDate":"%s","value":%s,"weight":450}
                """.formatted(datePurchase, birthDate, value);
    }

    @Test
    @DisplayName("POST .../animalPurchases deve criar compra e retornar 200")
    void shouldCreateAnimalPurchases() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateAnimalPurchasesOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), LocalDate.of(2023, 3, 10), BigDecimal.valueOf(1500), BigDecimal.valueOf(450));
        when(createAnimalPurchasesUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10", "2023-03-10", "1500")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(1500))
                .andExpect(jsonPath("$.weight").value(450))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateAnimalPurchasesCommand.class);
        verify(createAnimalPurchasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().datePurchase());
        assertEquals(BigDecimal.valueOf(1500), captor.getValue().value());
        assertEquals(new BigDecimal("450"), captor.getValue().weight());
    }

    @Test
    @DisplayName("PUT .../animalPurchases/{id} deve atualizar compra e retornar 200")
    void shouldUpdateAnimalPurchases() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateAnimalPurchasesOutput(id, animalId, LocalDate.of(2024, 4, 1),
                LocalDate.of(2023, 4, 1), BigDecimal.valueOf(2000), BigDecimal.valueOf(450));
        when(updateAnimalPurchasesUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-04-01", "2023-04-01", "2000")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(2000))
                .andExpect(jsonPath("$.weight").value(450))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateAnimalPurchasesCommand.class);
        verify(updateAnimalPurchasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalPurchasesId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("DELETE .../animalPurchases/{id} deve deletar compra e retornar 204")
    void shouldDeleteAnimalPurchases() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteAnimalPurchasesUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteAnimalPurchasesCommand.class);
        verify(deleteAnimalPurchasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalPurchasesId());
        assertEquals("tecnico@idr.com", captor.getValue().username());
    }

    @Test
    @DisplayName("GET .../animalPurchases/{id} deve retornar compra e 200")
    void shouldGetAnimalPurchasesById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetAnimalPurchasesByIdOutput(id, animalId, LocalDate.of(2024, 3, 10),
                LocalDate.of(2023, 3, 10), BigDecimal.valueOf(1500), BigDecimal.valueOf(450));
        when(getAnimalPurchasesByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetAnimalPurchasesByIdCommand.class);
        verify(getAnimalPurchasesByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalPurchasesId());
    }

    @Test
    @DisplayName("GET .../animalPurchases/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchAnimalPurchasesScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var animalPurchasesFromAnimal = new ListAnimalPurchasesOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), LocalDate.of(2023, 3, 10), BigDecimal.valueOf(1500), BigDecimal.valueOf(450));
        final var pagination = new Pagination<>(0, 10, 1L, List.of(animalPurchasesFromAnimal));
        when(listAnimalPurchasesUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListAnimalPurchasesCommand.class);
        verify(listAnimalPurchasesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../animalPurchases/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListAnimalPurchasesOutput>(0, 10, 0L, List.of());
        when(listAnimalPurchasesUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListAnimalPurchasesCommand.class);
        verify(listAnimalPurchasesUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalPurchasesByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (compra não pertence ao animal)")
    void shouldReturn400WhenAnimalPurchasesNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalPurchasesByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Compra não encontrada"));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }
}
