package br.gov.pr.idr.domain.inventory_resource.product;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

/** Produto do catálogo global, com princípio ativo e categoria obrigatórios. */
public class Product extends AggregateRoot<ProductID> {

    private String name;
    private String description;
    private String applicationWay;
    private ActivePrincipleID activePrincipleId;
    private ProductCategoryID categoryId;
    private Long version;

    protected Product(final ProductID id, final String name, final String description, final String applicationWay,
                       final ActivePrincipleID activePrincipleId, final ProductCategoryID categoryId) {
        super(id);
        this.name = name;
        this.description = description;
        this.applicationWay = applicationWay;
        this.activePrincipleId = activePrincipleId;
        this.categoryId = categoryId;
    }

    public static Product with(final ProductID id, final String name, final String description,
                                final String applicationWay, final ActivePrincipleID activePrincipleId,
                                final ProductCategoryID categoryId) {
        return new Product(id, name, description, applicationWay, activePrincipleId, categoryId);
    }

    public static Product create(final String name, final String description, final String applicationWay,
                                  final ActivePrincipleID activePrincipleId, final ProductCategoryID categoryId) {
        final var product = new Product(ProductID.unique(), name, description, applicationWay, activePrincipleId, categoryId);
        product.selfValidate();
        return product;
    }

    public Product update(final String name, final String description, final String applicationWay,
                           final ActivePrincipleID activePrincipleId, final ProductCategoryID categoryId) {
        this.name = name;
        this.description = description;
        this.applicationWay = applicationWay;
        this.activePrincipleId = activePrincipleId;
        this.categoryId = categoryId;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("O nome do produto é obrigatório"));
        }
        if (this.description == null || this.description.isBlank()) {
            handler.append(DomainError.from("A descrição do produto é obrigatória"));
        }
        if (this.applicationWay == null || this.applicationWay.isBlank()) {
            handler.append(DomainError.from("A via de aplicação do produto é obrigatória"));
        }
        if (this.activePrincipleId == null) {
            handler.append(DomainError.from("O princípio ativo do produto é obrigatório"));
        }
        if (this.categoryId == null) {
            handler.append(DomainError.from("A categoria do produto é obrigatória"));
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getApplicationWay() {
        return applicationWay;
    }

    public ActivePrincipleID getActivePrincipleId() {
        return activePrincipleId;
    }

    public ProductCategoryID getCategoryId() {
        return categoryId;
    }

    public Long getVersion() {
        return version;
    }

    public Product withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
