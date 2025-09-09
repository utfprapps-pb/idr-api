package br.edu.utfpr.ProjetoIDRAPI.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSpecExecutor {
}
