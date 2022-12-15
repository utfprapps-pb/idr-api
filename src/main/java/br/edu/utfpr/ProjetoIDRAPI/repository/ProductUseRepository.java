package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUseRepository extends JpaRepository<ProductUse, Long> {
    List<ProductUse> findAllByPropertyId(Long id);
}
