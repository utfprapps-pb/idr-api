package br.gov.pr.idr.legacy.entity.productcategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, ProductCategorySpecExecutor {
}
