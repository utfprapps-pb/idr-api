package br.gov.pr.idr.application.inventory_resource.product_category.create;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;

@CommandUseCase
public class CreateProductCategoryUseCase extends UseCase<CreateProductCategoryCommand, CreateProductCategoryOutput> {

    private final ProductCategoryGateway productCategoryGateway;

    public CreateProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    @Override
    public CreateProductCategoryOutput execute(CreateProductCategoryCommand command) {
        final var category = ProductCategory.create(command.description());
        return CreateProductCategoryOutput.from(productCategoryGateway.save(category));
    }
}
