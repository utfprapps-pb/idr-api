package br.edu.utfpr.ProjetoIDRAPI.entity.productcategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, ProductCategorySpecExecutor {
}
