package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.specexecutor.CultureSpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Culture;

public interface CultureRepository extends JpaRepository<Culture, Long>, CultureSpecExecutor {
	Culture findByCultureName(String name);
}
