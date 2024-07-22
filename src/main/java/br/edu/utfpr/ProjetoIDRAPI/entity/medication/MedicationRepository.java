package br.edu.utfpr.ProjetoIDRAPI.entity.medication;

import br.edu.utfpr.ProjetoIDRAPI.entity.medication.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
