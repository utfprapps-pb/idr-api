package br.gov.pr.idr.legacy.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSpecExecutor {
}
