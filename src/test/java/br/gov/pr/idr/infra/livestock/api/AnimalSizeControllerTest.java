package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeOutput;
import br.gov.pr.idr.application.livestock.animalsize.create.CreateAnimalSizeUseCase;
import br.gov.pr.idr.application.livestock.animalsize.delete.DeleteAnimalSizeUseCase;
import br.gov.pr.idr.application.livestock.animalsize.retrieve.list.ListAnimalSizeOutput;
import br.gov.pr.idr.application.livestock.animalsize.retrieve.list.ListAnimalSizeUseCase;
import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeOutput;
import br.gov.pr.idr.application.livestock.animalsize.update.UpdateAnimalSizeUseCase;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalSizeController")
class AnimalSizeControllerTest {

    @Mock CreateAnimalSizeUseCase createAnimalSizeUseCase;
    @Mock UpdateAnimalSizeUseCase updateAnimalSizeUseCase;
    @Mock ListAnimalSizeUseCase listAnimalSizeUseCase;
    @Mock DeleteAnimalSizeUseCase deleteAnimalSizeUseCase;
    @InjectMocks AnimalSizeController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /animal-sizes deve criar porte e retornar 200")
    void shouldCreateAnimalSize() throws Exception {
        final var output = new CreateAnimalSizeOutput(UUID.randomUUID(), "Grande");
        when(createAnimalSizeUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/animal-sizes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sizeName\":\"Grande\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sizeName").value("Grande"));
    }

    @Test
    @DisplayName("PUT /animal-sizes/{id} deve atualizar porte e retornar 200")
    void shouldUpdateAnimalSize() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new UpdateAnimalSizeOutput(id, "Média");
        when(updateAnimalSizeUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/animal-sizes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sizeName\":\"Média\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sizeName").value("Média"));
    }

    @Test
    @DisplayName("POST /animal-sizes com nome acima de 255 caracteres deve retornar 400")
    void shouldRejectCreateWithSizeNameTooLong() throws Exception {
        mockMvc.perform(post("/animal-sizes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sizeName\":\"" + "a".repeat(256) + "\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(createAnimalSizeUseCase);
    }

    @Test
    @DisplayName("PUT /animal-sizes/{id} com nome acima de 255 caracteres deve retornar 400")
    void shouldRejectUpdateWithSizeNameTooLong() throws Exception {
        mockMvc.perform(put("/animal-sizes/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sizeName\":\"" + "a".repeat(256) + "\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(updateAnimalSizeUseCase);
    }

    @Test
    @DisplayName("DELETE /animal-sizes/{id} deve deletar porte e retornar 204")
    void shouldDeleteAnimalSize() throws Exception {
        doNothing().when(deleteAnimalSizeUseCase).execute(any());

        mockMvc.perform(delete("/animal-sizes/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /animal-sizes/search deve retornar paginação de portes")
    void shouldListAnimalSizes() throws Exception {
        final var id = UUID.randomUUID();
        final var pagination = new Pagination<>(0, 10, 1L, List.of(new ListAnimalSizeOutput(id, "Grande")));
        when(listAnimalSizeUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/animal-sizes/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].sizeName").value("Grande"));

        final var captor = ArgumentCaptor.forClass(SearchQuery.class);
        verify(listAnimalSizeUseCase).execute(captor.capture());
        assertEquals("sizeName", captor.getValue().sort());
    }
}
