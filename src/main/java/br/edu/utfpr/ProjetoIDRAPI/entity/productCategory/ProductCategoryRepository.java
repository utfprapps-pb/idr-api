package br.edu.utfpr.ProjetoIDRAPI.entity.productCategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, ProductCategorySpecExecutor {
}
