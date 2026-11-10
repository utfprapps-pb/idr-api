package br.gov.pr.idr.application.inventory_resource.product_category;

import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryCommand;
import br.gov.pr.idr.application.inventory_resource.product_category.update.UpdateProductCategoryUseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateProductCategoryUseCase")
class UpdateProductCategoryUseCaseTest {

    @Mock ProductCategoryGateway gateway;
    @InjectMocks UpdateProductCategoryUseCase useCase;

    @Test
    void shouldThrowWhenUpdatingNonExistentCategory() {
        when(gateway.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> useCase.execute(new UpdateProductCategoryCommand(UUID.randomUUID(), "x")));
    }
}
