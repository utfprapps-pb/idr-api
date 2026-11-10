package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleOutput;
import br.gov.pr.idr.application.inventory_resource.active_principle.create.CreateActivePrincipleUseCase;
import br.gov.pr.idr.application.inventory_resource.active_principle.delete.DeleteActivePrincipleUseCase;
import br.gov.pr.idr.application.inventory_resource.active_principle.retrieve.list.ListActivePrincipleOutput;
import br.gov.pr.idr.application.inventory_resource.active_principle.retrieve.list.ListActivePrincipleUseCase;
import br.gov.pr.idr.application.inventory_resource.active_principle.update.UpdateActivePrincipleOutput;
import br.gov.pr.idr.application.inventory_resource.active_principle.update.UpdateActivePrincipleUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
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
@DisplayName("ActivePrincipleController")
class ActivePrincipleControllerTest {

    @Mock CreateActivePrincipleUseCase createActivePrincipleUseCase;
    @Mock UpdateActivePrincipleUseCase updateActivePrincipleUseCase;
    @Mock ListActivePrincipleUseCase listActivePrincipleUseCase;
    @Mock DeleteActivePrincipleUseCase deleteActivePrincipleUseCase;
    @InjectMocks ActivePrincipleController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /activePrinciples deve criar princípio ativo e retornar 200")
    void shouldCreateActivePrinciple() throws Exception {
        final var output = new CreateActivePrincipleOutput(UUID.randomUUID(), "Ivermectina");
        when(createActivePrincipleUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/activePrinciples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ivermectina\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivermectina"));
    }

    @Test
    @DisplayName("PUT /activePrinciples/{id} deve atualizar princípio ativo e retornar 200")
    void shouldUpdateActivePrinciple() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new UpdateActivePrincipleOutput(id, "Albendazol");
        when(updateActivePrincipleUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/activePrinciples/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Albendazol\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Albendazol"));
    }

    @Test
    @DisplayName("DELETE /activePrinciples/{id} deve deletar princípio ativo e retornar 204")
    void shouldDeleteActivePrinciple() throws Exception {
        doNothing().when(deleteActivePrincipleUseCase).execute(any());

        mockMvc.perform(delete("/activePrinciples/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /activePrinciples/search deve retornar paginação de princípios ativos")
    void shouldListActivePrinciples() throws Exception {
        final var id = UUID.randomUUID();
        final var pagination = new Pagination<>(0, 10, 1L, List.of(new ListActivePrincipleOutput(id, "Ivermectina")));
        when(listActivePrincipleUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/activePrinciples/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
