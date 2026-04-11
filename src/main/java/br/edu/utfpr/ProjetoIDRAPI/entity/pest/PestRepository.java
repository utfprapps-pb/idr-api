package br.edu.utfpr.ProjetoIDRAPI.entity.pest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PestRepository extends JpaRepository<Pest, Long>, PestSpecExecutor {
	Pest findByName(String name);
}
