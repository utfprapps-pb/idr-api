package br.edu.utfpr.ProjetoIDRAPI.entity.mastitis;

import br.edu.utfpr.ProjetoIDRAPI.entity.mastitis.Mastitis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MastitisRepository extends JpaRepository<Mastitis, Long> {
}
