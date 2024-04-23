package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.specexecutor.DiseaaseSpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Disease;

public interface DiseaseRepository extends JpaRepository<Disease, Long>, DiseaaseSpecExecutor {
	Disease findByDiseaseName(String name);
}
