package br.gov.pr.idr.infra.inventory_resource.product.persistence;

import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleID;
import br.gov.pr.idr.domain.inventory_resource.product.Product;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
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
@Table(name = "inventory_resource_product")
@SQLRestriction("deleted_at is null")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductJPAEntity extends SyncableJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "application_way", nullable = false)
    private String applicationWay;

    @Column(name = "active_principle_id", nullable = false)
    private UUID activePrincipleId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    public static ProductJPAEntity from(final Product product) {
        final var entity = new ProductJPAEntity(
                product.getId().id(),
                product.getName(),
                product.getDescription(),
                product.getApplicationWay(),
                product.getActivePrincipleId().id(),
                product.getCategoryId().id()
        );
        entity.applyVersion(product.getVersion());
        return entity;
    }

    public Product toAggregate() {
        final var aggregate = Product.with(
                ProductID.from(this.id),
                this.name,
                this.description,
                this.applicationWay,
                ActivePrincipleID.from(this.activePrincipleId),
                ProductCategoryID.from(this.categoryId)
        );
        return aggregate.withVersion(this.getVersion());
    }
}
