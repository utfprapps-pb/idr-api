package br.gov.pr.idr.infra.inventory_resource.api;

import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryOutput;
import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryUseCase;
import br.gov.pr.idr.application.inventory_resource.product_category.delete.DeleteProductCategoryUseCase;
import br.gov.pr.idr.application.inventory_resource.product_category.retrieve.list.ListProductCategoryOutput;
import br.gov.pr.idr.application.inventory_resource.product_category.retrieve.list.ListProductCategoryUseCase;
import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryOutput;
import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryUseCase;
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
@DisplayName("ProductCategoryController")
class ProductCategoryControllerTest {

    @Mock CreateProductCategoryUseCase createProductCategoryUseCase;
    @Mock UpdateProductCategoryUseCase updateProductCategoryUseCase;
    @Mock ListProductCategoryUseCase listProductCategoryUseCase;
    @Mock DeleteProductCategoryUseCase deleteProductCategoryUseCase;
    @InjectMocks ProductCategoryController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /productCategories deve criar categoria de produto e retornar 200")
    void shouldCreateProductCategory() throws Exception {
        final var output = new CreateProductCategoryOutput(UUID.randomUUID(), "Ração");
        when(createProductCategoryUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(post("/productCategories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Ração\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Ração"));
    }

    @Test
    @DisplayName("PUT /productCategories/{id} deve atualizar categoria de produto e retornar 200")
    void shouldUpdateProductCategory() throws Exception {
        final var id = UUID.randomUUID();
        final var output = new UpdateProductCategoryOutput(id, "Medicamentos");
        when(updateProductCategoryUseCase.execute(any())).thenReturn(output);

        mockMvc.perform(put("/productCategories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Medicamentos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Medicamentos"));
    }

    @Test
    @DisplayName("DELETE /productCategories/{id} deve deletar categoria de produto e retornar 204")
    void shouldDeleteProductCategory() throws Exception {
        doNothing().when(deleteProductCategoryUseCase).execute(any());

        mockMvc.perform(delete("/productCategories/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /productCategories/search deve retornar paginação de categorias de produto")
    void shouldListProductCategories() throws Exception {
        final var id = UUID.randomUUID();
        final var pagination = new Pagination<>(0, 10, 1L, List.of(new ListProductCategoryOutput(id, "Ração")));
        when(listProductCategoryUseCase.execute(any())).thenReturn(pagination);

        mockMvc.perform(get("/productCategories/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
