package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.specexecutor.ActivePrincipleSpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivePrincipleRepository extends JpaRepository<ActivePrinciple, Long>, ActivePrincipleSpecExecutor {
}
