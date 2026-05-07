package br.gov.pr.idr.legacy.entity.disease;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, Long>, DiseaseSpecExecutor {
	Disease findByName(String name);
}
