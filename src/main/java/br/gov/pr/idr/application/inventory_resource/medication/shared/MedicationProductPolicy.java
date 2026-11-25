package br.gov.pr.idr.application.inventory_resource.medication.shared;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrinciple;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

import java.util.UUID;

/**
 * Define o produto registrado numa medicação. Com {@code productId}, o produto precisa existir no catálogo e o nome
 * e o princípio ativo são copiados dele (o que o cliente mandou nesses campos é ignorado). Sem {@code productId},
 * vale o que foi digitado, sem espaços nas pontas e com texto em branco tratado como ausente ({@code null}); a
 * obrigatoriedade do nome é validada pelo agregado {@code Medication}.
 */
public final class MedicationProductPolicy {

    private MedicationProductPolicy() {
    }

    public static MedicationProduct resolve(final ProductGateway productGateway,
                                            final ActivePrincipleGateway activePrincipleGateway,
                                            final UUID productId, final String typedProductName,
                                            final String typedActivePrinciple) {
        if (productId == null) {
            return new MedicationProduct(null, normalize(typedProductName), normalize(typedActivePrinciple));
        }

        final var id = ProductID.from(productId);
        final var product = productGateway.findById(id).orElseThrow(() -> new NotificationException(
                "Produto com o id %s não encontrado".formatted(productId), NotificationValidation.create()));
        final var activePrinciple = product.getActivePrincipleId() == null ? null
                : activePrincipleGateway.findById(product.getActivePrincipleId())
                        .map(ActivePrinciple::getName)
                        .orElse(null);

        return new MedicationProduct(id, product.getName(), activePrinciple);
    }

    private static String normalize(final String typed) {
        return typed == null || typed.isBlank() ? null : typed.strip();
    }

    public record MedicationProduct(ProductID productId, String productName, String activePrinciple) {
    }
}
