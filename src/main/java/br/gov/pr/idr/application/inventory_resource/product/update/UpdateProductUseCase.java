package br.gov.pr.idr.application.inventory_resource.product.update;

import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryGateway;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

@CommandUseCase
public class UpdateProductUseCase extends UseCase<UpdateProductCommand, UpdateProductOutput> {

    private final ProductGateway productGateway;
    private final ActivePrincipleGateway activePrincipleGateway;
    private final ProductCategoryGateway productCategoryGateway;

    public UpdateProductUseCase(ProductGateway productGateway, ActivePrincipleGateway activePrincipleGateway,
                                 ProductCategoryGateway productCategoryGateway) {
        this.productGateway = productGateway;
        this.activePrincipleGateway = activePrincipleGateway;
        this.productCategoryGateway = productCategoryGateway;
    }

    @Override
    public UpdateProductOutput execute(final UpdateProductCommand command) {
        final var productId = ProductID.from(command.productId());
        final var activePrincipleId = ActivePrincipleID.from(command.activePrincipleId());
        final var categoryId = ProductCategoryID.from(command.categoryId());
        final var name = command.name();
        final var description = command.description();
        final var applicationWay = command.applicationWay();
        final var notification = NotificationValidation.create();

        final var product = productGateway.findById(productId).orElseThrow(
                () -> new NotificationException("Produto com o id %s não encontrado".formatted(productId.id()),
                        notification)).update(name, description, applicationWay, activePrincipleId, categoryId);

        if (!activePrincipleGateway.existsById(activePrincipleId)) {
            throw new NotificationException(
                    "Princípio ativo com o id %s não encontrado".formatted(activePrincipleId.id()), notification);
        }
        if (!productCategoryGateway.existsById(categoryId)) {
            throw new NotificationException(
                    "Categoria de produto com o id %s não encontrada".formatted(categoryId.id()), notification);
        }
        return UpdateProductOutput.from(productGateway.update(product));
    }
}
