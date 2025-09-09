package br.edu.utfpr.ProjetoIDRAPI.entity.landproduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LandProductRepository extends JpaRepository<LandProduct, Long> {
    List<LandProduct> findAllByPropertyId(Long id);
}
