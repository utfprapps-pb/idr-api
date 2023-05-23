package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
