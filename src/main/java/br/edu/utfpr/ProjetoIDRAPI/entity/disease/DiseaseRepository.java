package br.edu.utfpr.ProjetoIDRAPI.entity.disease;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepository extends JpaRepository<Disease, Long>, DiseaseSpecExecutor {
	Disease findByDiseaseName(String name);
}
