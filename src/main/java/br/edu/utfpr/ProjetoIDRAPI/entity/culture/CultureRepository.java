package br.edu.utfpr.ProjetoIDRAPI.entity.culture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CultureRepository extends JpaRepository<Culture, Long>, CultureSpecExecutor {
	Culture findByCultureName(String name);
}
