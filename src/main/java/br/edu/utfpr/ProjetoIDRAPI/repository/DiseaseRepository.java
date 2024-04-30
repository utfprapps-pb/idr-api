package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.specexecutor.DiseaseSpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, Long>, DiseaseSpecExecutor {
	Disease findByDiseaseName(String name);
}
