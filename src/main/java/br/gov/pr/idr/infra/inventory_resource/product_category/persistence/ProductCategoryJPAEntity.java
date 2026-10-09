package br.gov.pr.idr.infra.inventory_resource.product_category.persistence;

import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategory;
import br.gov.pr.idr.domain.inventory_resource.product_category.ProductCategoryID;
import br.gov.pr.idr.infra.shared.persistence.SyncableJPAEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "inventory_resource_product_category")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCategoryJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String description;

    public static ProductCategoryJPAEntity from(final ProductCategory category) {
        final var entity = new ProductCategoryJPAEntity(
                category.getId().id(),
                category.getDescription()
        );
        entity.applyVersion(category.getVersion());
        return entity;
    }

    public ProductCategory toAggregate() {
        final var aggregate = ProductCategory.with(
                ProductCategoryID.from(this.id),
                this.description
        );
        return aggregate.withVersion(this.getVersion());
    }
}
