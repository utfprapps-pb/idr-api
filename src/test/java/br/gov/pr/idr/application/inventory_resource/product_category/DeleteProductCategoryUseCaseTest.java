package br.gov.pr.idr.application.inventory_resource.product_category;

import br.gov.pr.idr.application.inventory_resource.product_category.delete.DeleteProductCategoryUseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteProductCategoryUseCase")
class DeleteProductCategoryUseCaseTest {

    @Mock ProductCategoryGateway gateway;
    @InjectMocks DeleteProductCategoryUseCase useCase;

    @Test
    @DisplayName("deve deletar categoria de produto existente")
    void shouldDeleteExistingCategory() {
        when(gateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(gateway).deleteById(any());
    }

    @Test
    void shouldThrowWhenDeletingNonExistentCategory() {
        when(gateway.existsById(any())).thenReturn(false);
        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
    }
}
