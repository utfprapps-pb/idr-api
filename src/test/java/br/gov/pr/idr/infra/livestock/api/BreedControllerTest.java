package br.gov.pr.idr.infra.livestock.api;

import br.gov.pr.idr.application.livestock.breed.create.CreateBreedOutput;
import br.gov.pr.idr.application.livestock.breed.create.CreateBreedUseCase;
import br.gov.pr.idr.application.livestock.breed.delete.DeleteBreedUseCase;
import br.gov.pr.idr.application.livestock.breed.retrieve.list.ListBreedOutput;
import br.gov.pr.idr.application.livestock.breed.retrieve.list.ListBreedUseCase;
import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedOutput;
import br.gov.pr.idr.application.livestock.breed.update.UpdateBreedUseCase;
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
@DisplayName("BreedController")
class BreedControllerTest {

    @Mock CreateBreedUseCase createBreedUseCase;
    @Mock UpdateBreedUseCase updateBreedUseCase;
    @Mock ListBreedUseCase listBreedUseCase;
    @Mock DeleteBreedUseCase deleteBreedUseCase;
    @InjectMocks BreedController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /breeds deve criar raça e retornar 200")
    void shouldCreateBreed() throws Exception {
        final var output = new CreateBreedOutput(UUID.randomUUID(), "Nelore");
        when(createBreedUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/breeds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"breedName\":\"Nelore\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.breedName").value("Nelore"));
    }

    @Test
    @DisplayName("PUT /breeds/{id} deve atualizar raça e retornar 200")
    void shouldUpdateBreed() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new UpdateBreedOutput(id, "Angus");
        when(updateBreedUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/breeds/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"breedName\":\"Angus\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.breedName").value("Angus"));
    }

    @Test
    @DisplayName("DELETE /breeds/{id} deve deletar raça e retornar 204")
    void shouldDeleteBreed() throws Exception {
        doNothing().when(deleteBreedUseCase).execute(any());

        mockMvc.perform(delete("/breeds/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /breeds/search deve retornar paginação de raças")
    void shouldListBreeds() throws Exception {
        final var id = UUID.randomUUID();
        final var pagination = new Pagination<>(0, 10, 1L, List.of(new ListBreedOutput(id, "Nelore")));
        when(listBreedUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/breeds/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
