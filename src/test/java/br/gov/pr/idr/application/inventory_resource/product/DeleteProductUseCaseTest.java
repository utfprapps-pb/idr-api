package br.gov.pr.idr.application.inventory_resource.product;

import br.gov.pr.idr.application.inventory_resource.product.delete.DeleteProductUseCase;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteProductUseCase")
class DeleteProductUseCaseTest {

    @Mock ProductGateway productGateway;
    @InjectMocks DeleteProductUseCase useCase;

    @Test
    @DisplayName("deve deletar produto existente")
    void shouldDeleteExistingProduct() {
        when(productGateway.existsById(any())).thenReturn(true);

        useCase.execute(UUID.randomUUID());

        verify(productGateway).deleteById(any());
    }

    @Test
    @DisplayName("deve lançar NotFoundException quando produto não existe")
    void shouldThrowWhenProductNotFound() {
        when(productGateway.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> useCase.execute(UUID.randomUUID()));
        verify(productGateway, never()).deleteById(any());
    }
}
