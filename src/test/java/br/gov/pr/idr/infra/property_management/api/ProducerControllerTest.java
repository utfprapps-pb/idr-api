package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.property_management.producer.create.CreateProducerOutput;
import br.gov.pr.idr.application.property_management.producer.create.CreateProducerUseCase;
import br.gov.pr.idr.application.property_management.producer.retrieve.get.GetProducerByIdOutput;
import br.gov.pr.idr.application.property_management.producer.retrieve.get.GetProducerByIdUseCase;
import br.gov.pr.idr.application.property_management.producer.retrieve.search.SearchProducerOutput;
import br.gov.pr.idr.application.property_management.producer.retrieve.search.SearchProducerUseCase;
import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerOutput;
import br.gov.pr.idr.application.property_management.producer.update.UpdateProducerUseCase;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProducerController")
class ProducerControllerTest {

    @Mock CreateProducerUseCase createProducerUseCase;
    @Mock UpdateProducerUseCase updateProducerUseCase;
    @Mock GetProducerByIdUseCase getProducerByIdUseCase;
    @Mock SearchProducerUseCase searchProducerUseCase;
    @InjectMocks ProducerController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /v1/producers deve criar produtor e retornar 200")
    void shouldCreateProducer() throws Exception {
        final var output = new CreateProducerOutput(UUID.randomUUID(), "João Agricultor");
        when(createProducerUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/v1/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"João Agricultor\",\"cpf\":\"529.982.247-25\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Agricultor"));
    }

    @Test
    @DisplayName("PUT /v1/producers/{id} deve atualizar produtor e retornar 200")
    void shouldUpdateProducer() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new UpdateProducerOutput(id, "João Atualizado");
        when(updateProducerUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/v1/producers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"João Atualizado\",\"cpf\":\"529.982.247-25\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Atualizado"));
    }

    @Test
    @DisplayName("GET /v1/producers/{id} deve retornar produtor por ID")
    void shouldGetProducerById() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new GetProducerByIdOutput(id, "João", "529.982.247-25");
        when(getProducerByIdUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(get("/v1/producers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João"));
    }

    @Test
    @DisplayName("GET /v1/producers/search deve retornar paginação de produtores")
    void shouldSearchProducers() throws Exception {
        final var output = new SearchProducerOutput(UUID.randomUUID(), "João");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(output));
        when(searchProducerUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/v1/producers/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
