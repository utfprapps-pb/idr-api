package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.specexecutor.PlagueSpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Plague;

public interface PlagueRepository extends JpaRepository<Plague, Long>, PlagueSpecExecutor {
	Plague findByPlagueName(String name);
}
