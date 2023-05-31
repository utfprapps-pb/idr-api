package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Insemination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InseminationRepository extends JpaRepository<Insemination, Long> {
}
