package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.property.create.CreatePropertyOutput;
import br.gov.pr.idr.application.property_management.property.create.CreatePropertyUseCase;
import br.gov.pr.idr.application.property_management.property.delete.DeletePropertyUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.get.GetPropertyByIdUseCase;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyOutput;
import br.gov.pr.idr.application.property_management.property.retrieve.search.SearchPropertyUseCase;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyOutput;
import br.gov.pr.idr.application.property_management.property.update.UpdatePropertyUseCase;
import br.gov.pr.idr.domain.property_management.property.query.GetPropertyQueryResult;
import br.gov.pr.idr.domain.shared.search.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PropertyController")
class PropertyControllerTest {

    @Mock CreatePropertyUseCase createPropertyUseCase;
    @Mock UpdatePropertyUseCase updatePropertyUseCase;
    @Mock GetPropertyByIdUseCase getPropertyByIdUseCase;
    @Mock SearchPropertyUseCase searchPropertyUseCase;
    @Mock DeletePropertyUseCase deletePropertyUseCase;
    @InjectMocks PropertyController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/properties deve criar propriedade e retornar 200")
    void shouldCreateProperty() throws Exception {
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new CreatePropertyOutput(UUID.randomUUID(), "Fazenda Boa Vista");
        when(createPropertyUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/v1/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Fazenda Boa Vista\",\"latitude\":\"-25.4\",\"longitude\":\"-49.2\"," +
                                "\"producerId\":\"" + producerId + "\",\"cityId\":\"" + cityId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fazenda Boa Vista"));
    }

    @Test
    @DisplayName("PUT /v1/properties/{id} deve atualizar propriedade e retornar 200")
    void shouldUpdateProperty() throws Exception {
        final var id = UUID.randomUUID();
        final var producerId = UUID.randomUUID();
        final var cityId = UUID.randomUUID();
        final var output = new UpdatePropertyOutput(id, "Fazenda Atualizada");
        when(updatePropertyUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/v1/properties/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Fazenda Atualizada\",\"latitude\":\"-25.4\",\"longitude\":\"-49.2\"," +
                                "\"producerId\":\"" + producerId + "\",\"cityId\":\"" + cityId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fazenda Atualizada"));
    }

    @Test
    @DisplayName("GET /v1/properties/{id} deve retornar propriedade por ID")
    void shouldGetPropertyById() throws Exception {
        final var id = UUID.randomUUID();
        final var producer = new GetPropertyQueryResult.Producer(UUID.randomUUID(), "João");
        final var city = new GetPropertyQueryResult.City(UUID.randomUUID(), "Curitiba");
        final var result = new GetPropertyQueryResult(id, "Fazenda", BigDecimal.TEN, BigDecimal.ONE,
                0.0, 0.0, 0.0, 0.0, BigDecimal.valueOf(-25.4), BigDecimal.valueOf(-49.2),
                producer, city, List.of(), List.of());
        when(getPropertyByIdUseCase.execute(any())).thenReturn(result);

        mockMvc.perform(get("/v1/properties/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fazenda"));
    }

    @Test
    @DisplayName("GET /v1/properties/search deve retornar paginação de propriedades")
    void shouldSearchProperties() throws Exception {
        final var output = new SearchPropertyOutput(UUID.randomUUID(), "Fazenda",
                BigDecimal.TEN, BigDecimal.ONE, UUID.randomUUID(), UUID.randomUUID());
        final var pagination = new Pagination<>(0, 10, 1L, List.of(output));
        when(searchPropertyUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/v1/properties/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @DisplayName("DELETE /v1/properties/{id} deve deletar propriedade e retornar 204")
    void shouldDeleteProperty() throws Exception {
        doNothing().when(deletePropertyUseCase).execute(any());

        mockMvc.perform(delete("/v1/properties/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}
