package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.city.create.CreateCityOutput;
import br.gov.pr.idr.application.property_management.city.create.CreateCityUseCase;
import br.gov.pr.idr.application.property_management.city.delete.DeleteCityUseCase;
import br.gov.pr.idr.application.property_management.city.retrieve.list.ListCityUseCase;
import br.gov.pr.idr.application.property_management.city.update.UpdateCityOutput;
import br.gov.pr.idr.application.property_management.city.update.UpdateCityUseCase;
import br.gov.pr.idr.domain.property_management.city.query.ListCityQueryResult;
import br.gov.pr.idr.domain.property_management.city.vo.State;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CityController")
class CityControllerTest {

    @Mock CreateCityUseCase createCityUseCase;
    @Mock ListCityUseCase listCityUseCase;
    @Mock UpdateCityUseCase updateCityUseCase;
    @Mock DeleteCityUseCase deleteCityUseCase;
    @InjectMocks CityController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/cities deve criar cidade e retornar 200")
    void shouldCreateCity() throws Exception {
        final var regionId = UUID.randomUUID();
        final var output = new CreateCityOutput(UUID.randomUUID(), "Curitiba");
        when(createCityUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Curitiba\",\"state\":\"PR\",\"regionId\":\"" + regionId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Curitiba"));
    }

    @Test
    @DisplayName("GET /v1/cities/search deve retornar paginação de cidades")
    void shouldListCities() throws Exception {
        final var result = new ListCityQueryResult(UUID.randomUUID(), "Curitiba", State.PR, UUID.randomUUID(), "Região Sul");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(result));
        when(listCityUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/v1/cities/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @DisplayName("PUT /v1/cities/{id} deve atualizar cidade e retornar 200")
    void shouldUpdateCity() throws Exception {
        final var id = UUID.randomUUID();
        final var regionId = UUID.randomUUID();
        final var output = new UpdateCityOutput(id, "Londrina", State.PR, regionId);
        when(updateCityUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/v1/cities/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Londrina\",\"regionId\":\"" + regionId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Londrina"));
    }

    @Test
    @DisplayName("DELETE /v1/cities/{id} deve deletar cidade e retornar 204")
    void shouldDeleteCity() throws Exception {
        doNothing().when(deleteCityUseCase).execute(any());

        mockMvc.perform(delete("/v1/cities/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}
