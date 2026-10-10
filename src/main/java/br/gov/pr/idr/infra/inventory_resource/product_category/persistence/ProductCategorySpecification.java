package br.gov.pr.idr.infra.inventory_resource.product_category.persistence;

import br.gov.pr.idr.infra.shared.persistence.LikeSpecification;
import org.springframework.data.jpa.domain.Specification;

public class ProductCategorySpecification {

    private ProductCategorySpecification() {}

    public static Specification<ProductCategoryJPAEntity> withTerms(final String terms) {
        return LikeSpecification.withTerms(terms, "description");
    }
}
