package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductCategory;
import br.edu.utfpr.ProjetoIDRAPI.specexecutor.ProductCategorySpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, ProductCategorySpecExecutor {
}
