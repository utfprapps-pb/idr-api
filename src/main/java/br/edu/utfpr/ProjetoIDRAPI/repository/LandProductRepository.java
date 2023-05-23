package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.LandProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LandProductRepository extends JpaRepository<LandProduct, Long> {
    List<LandProduct> findAllByPropertyId(Long id);
}
