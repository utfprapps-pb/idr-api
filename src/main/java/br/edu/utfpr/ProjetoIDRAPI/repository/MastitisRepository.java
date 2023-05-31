package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Mastitis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MastitisRepository extends JpaRepository<Mastitis, Long> {
}
