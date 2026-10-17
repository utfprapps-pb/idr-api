package br.gov.pr.idr.application.inventory_resource.product.delete;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.UUID;

@CommandUseCase
public class DeleteProductUseCase extends VoidUseCase<UUID> {

    private final ProductGateway productGateway;

    public DeleteProductUseCase(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Override
    public void execute(final UUID id) {
        final var productId = ProductID.from(id);
        if (!productGateway.existsById(productId)) {
            throw NotFoundException.with(Product.class, productId);
        }
        productGateway.deleteById(productId);
    }
}
