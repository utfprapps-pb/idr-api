package br.gov.pr.idr.domain.inventory_resource.product_category;

import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

public class ProductCategory extends AggregateRoot<ProductCategoryID> {

    private String description;
    private Long version;

    protected ProductCategory(final ProductCategoryID id, final String description) {
        super(id);
        this.description = description;
    }

    public static ProductCategory create(final String description) {
        final var category = new ProductCategory(ProductCategoryID.unique(), description);
        category.selfValidate();
        return category;
    }

    public static ProductCategory with(final ProductCategoryID id, final String description) {
        return new ProductCategory(id, description);
    }

    public ProductCategory update(final String description) {
        this.description = description;
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.description == null || this.description.isBlank()) {
            handler.append(DomainError.from("Descrição da categoria de produto não pode ser vazia"));
        }
    }

    public String getDescription() {
        return description;
    }

    public Long getVersion() {
        return version;
    }

    public ProductCategory withVersion(final Long version) {
        this.version = version;
        return this;
    }
}
