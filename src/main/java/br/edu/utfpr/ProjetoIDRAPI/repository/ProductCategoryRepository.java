package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
