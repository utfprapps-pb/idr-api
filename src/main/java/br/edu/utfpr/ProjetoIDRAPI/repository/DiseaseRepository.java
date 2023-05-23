package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
	Disease findByDiseaseName(String name);
}
