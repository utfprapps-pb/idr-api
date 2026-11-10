package br.gov.pr.idr.application.inventory_resource.product_category.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeleteProductCategoryUseCase extends VoidUseCase<UUID> {

    private final ProductCategoryGateway productCategoryGateway;

    public DeleteProductCategoryUseCase(ProductCategoryGateway productCategoryGateway) {
        this.productCategoryGateway = productCategoryGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var categoryId = ProductCategoryID.from(id);
        if (!productCategoryGateway.existsById(categoryId)) {
            throw NotFoundException.with(ProductCategory.class, categoryId);
        }
        productCategoryGateway.deleteById(categoryId);
    }
}
