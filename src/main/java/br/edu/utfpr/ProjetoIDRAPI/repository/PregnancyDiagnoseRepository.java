package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.PregnancyDiagnose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PregnancyDiagnoseRepository extends JpaRepository<PregnancyDiagnose, Long> {
}
