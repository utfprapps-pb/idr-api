package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Product;
import br.edu.utfpr.ProjetoIDRAPI.specexecutor.ProductSpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSpecExecutor {
}
