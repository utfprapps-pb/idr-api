package br.gov.pr.idr.application.inventory_resource.product;

import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductCommand;
import br.gov.pr.idr.application.inventory_resource.product.create.CreateProductUseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductUseCase")
class CreateProductUseCaseTest {

    @Mock ProductGateway productGateway;
    @Mock ActivePrincipleGateway activePrincipleGateway;
    @Mock ProductCategoryGateway productCategoryGateway;
    @InjectMocks CreateProductUseCase useCase;

    @Test
    void shouldCreateWhenActivePrincipleAndCategoryExist() {
        when(activePrincipleGateway.existsById(any())).thenReturn(true);
        when(productCategoryGateway.existsById(any())).thenReturn(true);
        when(productGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new CreateProductCommand("P1", "D1", "W1",
                UUID.randomUUID(), UUID.randomUUID()));

        assertEquals("P1", output.name());
    }

    @Test
    void shouldThrowWhenActivePrincipleDoesNotExist() {
        when(activePrincipleGateway.existsById(any())).thenReturn(false);
        assertThrows(NotificationException.class, () -> useCase.execute(
                new CreateProductCommand("P1", "D1", "W1", UUID.randomUUID(), UUID.randomUUID())));
        verify(productGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {
        when(activePrincipleGateway.existsById(any())).thenReturn(true);
        when(productCategoryGateway.existsById(any())).thenReturn(false);
        assertThrows(NotificationException.class, () -> useCase.execute(
                new CreateProductCommand("P1", "D1", "W1", UUID.randomUUID(), UUID.randomUUID())));
    }
}
