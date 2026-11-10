package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductOutput;
import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductUseCase;
import br.gov.pr.idr.application.inventory_resource.product.delete.DeleteProductUseCase;
import br.gov.pr.idr.application.inventory_resource.product.retrieve.list.ListProductUseCase;
import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductOutput;
import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductUseCase;
import br.gov.pr.idr.domain.inventory_resource.product.query.ListProductQueryResult;
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
@DisplayName("ProductController")
class ProductControllerTest {

    @Mock CreateProductUseCase createProductUseCase;
    @Mock UpdateProductUseCase updateProductUseCase;
    @Mock ListProductUseCase listProductUseCase;
    @Mock DeleteProductUseCase deleteProductUseCase;
    @InjectMocks ProductController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /products deve criar produto e retornar 200")
    void shouldCreateProduct() throws Exception {
        final var activePrincipleId = UUID.randomUUID();
        final var categoryId = UUID.randomUUID();
        final var output = new CreateProductOutput(UUID.randomUUID(), "Vacina X");
        when(createProductUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Vacina X\",\"description\":\"Vacina contra febre aftosa\"," +
                                "\"applicationWay\":\"Intramuscular\",\"activePrincipleId\":\"" + activePrincipleId +
                                "\",\"categoryId\":\"" + categoryId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vacina X"));
    }

    @Test
    @DisplayName("GET /products/search deve retornar paginação de produtos")
    void shouldListProducts() throws Exception {
        final var result = new ListProductQueryResult(
                UUID.randomUUID(), "Vacina X", "Descrição", "Intramuscular",
                UUID.randomUUID(), "Aftosa Antígeno", UUID.randomUUID(), "Vacinas"
        );
        final var pagination = new Pagination<>(0, 10, 1L, List.of(result));
        when(listProductUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/products/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @DisplayName("PUT /products/{id} deve atualizar produto e retornar 200")
    void shouldUpdateProduct() throws Exception {
        final var id = UUID.randomUUID();
        final var activePrincipleId = UUID.randomUUID();
        final var categoryId = UUID.randomUUID();
        final var output = new UpdateProductOutput(id, "Vacina Y", "Descrição atualizada", "Oral",
                activePrincipleId, categoryId);
        when(updateProductUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Vacina Y\",\"description\":\"Descrição atualizada\"," +
                                "\"applicationWay\":\"Oral\",\"activePrincipleId\":\"" + activePrincipleId +
                                "\",\"categoryId\":\"" + categoryId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vacina Y"));
    }

    @Test
    @DisplayName("DELETE /products/{id} deve deletar produto e retornar 204")
    void shouldDeleteProduct() throws Exception {
        doNothing().when(deleteProductUseCase).execute(any());

        mockMvc.perform(delete("/products/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}
