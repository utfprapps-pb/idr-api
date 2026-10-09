package br.gov.pr.idr.application.inventory_resource.product;

import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductCommand;
import br.gov.pr.idr.application.inventory_resource.product.update.UpdateProductUseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProductUseCase")
class UpdateProductUseCaseTest {

    @Mock ProductGateway productGateway;
    @Mock ActivePrincipleGateway activePrincipleGateway;
    @Mock ProductCategoryGateway productCategoryGateway;
    @InjectMocks UpdateProductUseCase useCase;

    private final UUID productId = UUID.randomUUID();
    private final UUID activePrincipleId = UUID.randomUUID();
    private final UUID categoryId = UUID.randomUUID();

    @Test
    void shouldUpdateWhenActivePrincipleAndCategoryExist() {
        final var product = Product.create("P1", "D1", "W1",
                ActivePrincipleID.from(activePrincipleId), ProductCategoryID.from(categoryId));
        when(productGateway.findById(any())).thenReturn(Optional.of(product));
        when(activePrincipleGateway.existsById(any())).thenReturn(true);
        when(productCategoryGateway.existsById(any())).thenReturn(true);
        when(productGateway.update(any())).thenAnswer(inv -> inv.getArgument(0));

        final var output = useCase.execute(new UpdateProductCommand(productId, "P2", "D2", "W2",
                activePrincipleId, categoryId));

        assertEquals("P2", output.name());
        verify(productGateway).update(any());
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(productGateway.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(
                new UpdateProductCommand(productId, "P2", "D2", "W2", activePrincipleId, categoryId)));
        verify(productGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenActivePrincipleDoesNotExist() {
        final var product = Product.create("P1", "D1", "W1",
                ActivePrincipleID.from(activePrincipleId), ProductCategoryID.from(categoryId));
        when(productGateway.findById(any())).thenReturn(Optional.of(product));
        when(activePrincipleGateway.existsById(any())).thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(
                new UpdateProductCommand(productId, "P2", "D2", "W2", activePrincipleId, categoryId)));
        verify(productGateway, never()).update(any());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {
        final var product = Product.create("P1", "D1", "W1",
                ActivePrincipleID.from(activePrincipleId), ProductCategoryID.from(categoryId));
        when(productGateway.findById(any())).thenReturn(Optional.of(product));
        when(activePrincipleGateway.existsById(any())).thenReturn(true);
        when(productCategoryGateway.existsById(any())).thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(
                new UpdateProductCommand(productId, "P2", "D2", "W2", activePrincipleId, categoryId)));
        verify(productGateway, never()).update(any());
    }

    @Test
    void shouldThrowComprehensibleErrorWhenReferencedActivePrincipleWasSoftDeleted() {
        // activePrincipleGateway.existsById returns false here to simulate the @SQLRestriction
        // on ActivePrincipleJPAEntity excluding a soft-deleted row from existence checks.
        final var product = Product.create("P1", "D1", "W1",
                ActivePrincipleID.from(activePrincipleId), ProductCategoryID.from(categoryId));
        when(productGateway.findById(any())).thenReturn(Optional.of(product));
        when(activePrincipleGateway.existsById(any())).thenReturn(false);

        assertThrows(NotificationException.class, () -> useCase.execute(
                new UpdateProductCommand(productId, "P2", "D2", "W2", activePrincipleId, categoryId)));
        verify(productGateway, never()).update(any());
    }
}
