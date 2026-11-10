package br.gov.pr.idr.application.inventory_resource.product_category;

import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryCommand;
import br.gov.pr.idr.application.inventory_resource.product_category.create.CreateProductCategoryUseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateProductCategoryUseCase")
class CreateProductCategoryUseCaseTest {

    @Mock ProductCategoryGateway gateway;
    @InjectMocks CreateProductCategoryUseCase useCase;

    @Test
    void shouldCreateProductCategory() {
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(new CreateProductCategoryCommand("Antibiótico"));
        assertEquals("Antibiótico", output.description());
        verify(gateway).save(any());
    }
}
