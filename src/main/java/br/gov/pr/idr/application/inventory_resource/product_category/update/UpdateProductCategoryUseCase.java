package br.gov.pr.idr.application.inventory_resource.product_category.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateProductCategoryUseCase extends UseCase<UpdateProductCategoryCommand, UpdateProductCategoryOutput> {

    private final ProductCategoryGateway productCategoryGateway;

    public UpdateProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    @Override
    public UpdateProductCategoryOutput execute(UpdateProductCategoryCommand command) {
        final var id = ProductCategoryID.from(command.id());
        final var description = command.description();

        final var category = productCategoryGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(
                        "ProductCategory com o ID %s não encontrada".formatted(id.id())))
                .update(description);

        return UpdateProductCategoryOutput.from(productCategoryGateway.update(category));
    }
}
