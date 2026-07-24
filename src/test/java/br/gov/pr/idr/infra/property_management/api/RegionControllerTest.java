package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.region.create.CreateRegionOutput;
import br.gov.pr.idr.application.property_management.region.create.CreateRegionUseCase;
import br.gov.pr.idr.application.property_management.region.delete.DeleteRegionUseCase;
import br.gov.pr.idr.application.property_management.region.update.UpdateRegionOutput;
import br.gov.pr.idr.application.property_management.region.update.UpdateRegionUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.infra.property_management.region.models.list.ListRegionOutput;
import br.gov.pr.idr.infra.property_management.region.models.list.ListRegionUseCase;
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
@DisplayName("RegionController")
class RegionControllerTest {

    @Mock CreateRegionUseCase createRegionUseCase;
    @Mock UpdateRegionUseCase updateRegionUseCase;
    @Mock ListRegionUseCase listRegionUseCase;
    @Mock DeleteRegionUseCase deleteRegionUseCase;
    @InjectMocks RegionController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/regions deve criar região e retornar 200")
    void shouldCreateRegion() throws Exception {
        final var output = new CreateRegionOutput(UUID.randomUUID(), "Região Sul");
        when(createRegionUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Região Sul\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Região Sul"));
    }

    @Test
    @DisplayName("PUT /v1/regions/{id} deve atualizar região e retornar 200")
    void shouldUpdateRegion() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new UpdateRegionOutput(id, "Região Norte");
        when(updateRegionUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/v1/regions/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Região Norte\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Região Norte"));
    }

    @Test
    @DisplayName("DELETE /v1/regions/{id} deve deletar região e retornar 204")
    void shouldDeleteRegion() throws Exception {
        doNothing().when(deleteRegionUseCase).execute(any());

        mockMvc.perform(delete("/v1/regions/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /v1/regions/search deve retornar paginação de regiões")
    void shouldListRegions() throws Exception {
        final var id = UUID.randomUUID();
        final var pagination = new Pagination<>(0, 10, 1L, List.of(new ListRegionOutput(id, "Região Sul")));
        when(listRegionUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/v1/regions/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
