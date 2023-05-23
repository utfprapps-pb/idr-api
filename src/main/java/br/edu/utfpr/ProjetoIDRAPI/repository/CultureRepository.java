package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Culture;

public interface CultureRepository extends JpaRepository<Culture, Long> {
	Culture findByCultureName(String name);
}
