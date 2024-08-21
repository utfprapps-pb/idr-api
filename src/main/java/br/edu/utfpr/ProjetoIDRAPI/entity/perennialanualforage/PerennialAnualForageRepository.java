package br.edu.utfpr.ProjetoIDRAPI.entity.perennialanualforage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerennialAnualForageRepository extends JpaRepository<PerennialAnualForage, Long> {
    List<PerennialAnualForage> findAllByPropertyId(Long id);
}
