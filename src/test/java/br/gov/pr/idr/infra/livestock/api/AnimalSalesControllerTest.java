package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.domain.property_management.property.PropertySearchScope;
import br.gov.pr.idr.infra.iam.security.AuthenticatedUser;
import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesOutput;
import br.gov.pr.idr.application.livestock.animal_sales.create.CreateAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.delete.DeleteAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.delete.DeleteAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdCommand;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdOutput;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.get.GetAnimalSalesByIdUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.list.ListAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.list.ListAnimalSalesOutput;
import br.gov.pr.idr.application.livestock.animal_sales.retrieve.list.ListAnimalSalesUseCase;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesCommand;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesOutput;
import br.gov.pr.idr.application.livestock.animal_sales.update.UpdateAnimalSalesUseCase;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
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
@DisplayName("AnimalSalesController")
class AnimalSalesControllerTest {

    @Mock CreateAnimalSalesUseCase createAnimalSalesUseCase;
    @Mock UpdateAnimalSalesUseCase updateAnimalSalesUseCase;
    @Mock DeleteAnimalSalesUseCase deleteAnimalSalesUseCase;
    @Mock GetAnimalSalesByIdUseCase getAnimalSalesByIdUseCase;
    @Mock ListAnimalSalesUseCase listAnimalSalesUseCase;
    @InjectMocks AnimalSalesController controller;

    private MockMvc mockMvc;

    private static final UsernamePasswordAuthenticationToken AUTH =
            new UsernamePasswordAuthenticationToken(
                    new AuthenticatedUser("tecnico@idr.com", PropertySearchScope.unrestricted()), null, List.of());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private String basePath(final UUID propertyId, final UUID animalId) {
        return "/properties/" + propertyId + "/animals/" + animalId + "/sales";
    }

    private String createBody(final String dateSale, final String reason, final String value,
                               final String destination) {
        return """
                {"dateSale":"%s","reason":"%s","value":%s,"destination":"%s","weight":450}
                """.formatted(dateSale, reason, value, destination);
    }

    @Test
    @DisplayName("POST .../animalSales deve criar venda e retornar 200")
    void shouldCreateAnimalSales() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var output = new CreateAnimalSalesOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));
        when(createAnimalSalesUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10", "VOLUNTARIA", "1500", "ABATE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(1500))
                .andExpect(jsonPath("$.weight").value(450))
                .andExpect(jsonPath("$.animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(CreateAnimalSalesCommand.class);
        verify(createAnimalSalesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
        assertEquals(LocalDate.of(2024, 3, 10), captor.getValue().dateSale());
        assertEquals(Reason.VOLUNTARIA, captor.getValue().reason());
        assertEquals(BigDecimal.valueOf(1500), captor.getValue().value());
        assertEquals(Destination.ABATE, captor.getValue().destination());
        assertEquals(new BigDecimal("450"), captor.getValue().weight());
    }

    @Test
    @DisplayName("PUT .../animalSales/{id} deve atualizar venda e retornar 200")
    void shouldUpdateAnimalSales() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new UpdateAnimalSalesOutput(id, animalId, LocalDate.of(2024, 4, 1),
                Reason.EMERGENCIA, BigDecimal.valueOf(2000), Destination.PRODUCAO, BigDecimal.valueOf(450));
        when(updateAnimalSalesUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put(basePath(propertyId, animalId) + "/" + id)
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-04-01", "EMERGENCIA", "2000", "PRODUCAO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(2000))
                .andExpect(jsonPath("$.weight").value(450))
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(UpdateAnimalSalesCommand.class);
        verify(updateAnimalSalesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalSalesId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
    }

    @Test
    @DisplayName("DELETE .../animalSales/{id} deve deletar venda e retornar 204")
    void shouldDeleteAnimalSales() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        doNothing().when(deleteAnimalSalesUseCase).execute(any());

        mockMvc.perform(delete(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isNoContent());

        final var captor = ArgumentCaptor.forClass(DeleteAnimalSalesCommand.class);
        verify(deleteAnimalSalesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalSalesId());
        assertEquals(PropertySearchScope.unrestricted(), captor.getValue().scope());
    }

    @Test
    @DisplayName("GET .../animalSales/{id} deve retornar venda e 200")
    void shouldGetAnimalSalesById() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        final var output = new GetAnimalSalesByIdOutput(id, animalId, LocalDate.of(2024, 3, 10),
                Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));
        when(getAnimalSalesByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        final var captor = ArgumentCaptor.forClass(GetAnimalSalesByIdCommand.class);
        verify(getAnimalSalesByIdUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
        assertEquals(id, captor.getValue().animalSalesId());
    }

    @Test
    @DisplayName("GET .../animalSales/search escopa a busca pelo animalId (e propertyId) do path")
    void shouldSearchAnimalSalesScopedByAnimalIdInPath() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var animalSalesFromAnimal = new ListAnimalSalesOutput(UUID.randomUUID(), animalId,
                LocalDate.of(2024, 3, 10), Reason.VOLUNTARIA, BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450));
        final var pagination = new Pagination<>(0, 10, 1L, List.of(animalSalesFromAnimal));
        when(listAnimalSalesUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalId) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].animalId").value(animalId.toString()));

        final var captor = ArgumentCaptor.forClass(ListAnimalSalesCommand.class);
        verify(listAnimalSalesUseCase).execute(captor.capture());
        assertEquals(propertyId, captor.getValue().propertyId());
        assertEquals(animalId, captor.getValue().animalId());
    }

    @Test
    @DisplayName("GET .../animalSales/search com animalId diferente no path gera comando escopado "
            + "para o animal correspondente")
    void shouldScopeSearchToPathAnimalIdForDifferentAnimal() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalB = UUID.randomUUID();
        final var pagination = new Pagination<ListAnimalSalesOutput>(0, 10, 0L, List.of());
        when(listAnimalSalesUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get(basePath(propertyId, animalB) + "/search").principal(AUTH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));

        final var captor = ArgumentCaptor.forClass(ListAnimalSalesCommand.class);
        verify(listAnimalSalesUseCase).execute(captor.capture());
        assertEquals(animalB, captor.getValue().animalId());
    }

    @Test
    @DisplayName("deve retornar 403 quando o Use Case lança ForbiddenException (acesso negado à propriedade/animal)")
    void shouldReturn403WhenAccessIsForbidden() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalSalesByIdUseCase.execute(any()))
                .thenThrow(ForbiddenException.with("Usuário não tem acesso à propriedade com o ID " + propertyId));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deve retornar 400 quando o Use Case lança NotFoundException (venda não pertence ao animal)")
    void shouldReturn400WhenAnimalSalesNotFound() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();
        final var id = UUID.randomUUID();
        when(getAnimalSalesByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with("Venda não encontrada"));

        mockMvc.perform(get(basePath(propertyId, animalId) + "/" + id).principal(AUTH))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST .../animalSales com enum reason inválido deve retornar 400 "
            + "(HttpMessageNotReadableException tratada globalmente)")
    void shouldReturn400WhenReasonEnumIsInvalid() throws Exception {
        final var propertyId = UUID.randomUUID();
        final var animalId = UUID.randomUUID();

        mockMvc.perform(post(basePath(propertyId, animalId))
                        .principal(AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("2024-03-10", "INVALIDO", "1500", "ABATE")))
                .andExpect(status().isBadRequest());

        verify(createAnimalSalesUseCase, never()).execute(any());
    }
}
