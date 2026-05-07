package br.gov.pr.idr.legacy.entity.medication;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
