package br.edu.utfpr.ProjetoIDRAPI.entity.insemination;

import br.edu.utfpr.ProjetoIDRAPI.entity.insemination.Insemination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InseminationRepository extends JpaRepository<Insemination, Long> {
}
