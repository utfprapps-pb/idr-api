package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualFodders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerennialAnualFoddersRepository extends JpaRepository<PerennialAnualFodders, Long> {
    List<PerennialAnualFodders> findAllByPropertyId(Long id);
}
