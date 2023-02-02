package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualForage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerennialAnualForageRepository extends JpaRepository<PerennialAnualForage, Long> {
    List<PerennialAnualForage> findAllByPropertyId(Long id);
}
