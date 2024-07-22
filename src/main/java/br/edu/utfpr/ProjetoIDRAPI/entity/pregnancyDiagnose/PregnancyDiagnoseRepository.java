package br.edu.utfpr.ProjetoIDRAPI.entity.pregnancyDiagnose;

import br.edu.utfpr.ProjetoIDRAPI.entity.pregnancyDiagnose.PregnancyDiagnose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PregnancyDiagnoseRepository extends JpaRepository<PregnancyDiagnose, Long> {
}
