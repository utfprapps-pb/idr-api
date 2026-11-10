package br.gov.pr.idr.infra.inventory_resource.product_category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductCategoryJPARepository extends JpaRepository<ProductCategoryJPAEntity, UUID>,
        JpaSpecificationExecutor<ProductCategoryJPAEntity> {
}
