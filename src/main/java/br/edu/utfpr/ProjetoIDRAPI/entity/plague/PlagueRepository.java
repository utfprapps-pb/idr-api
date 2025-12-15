package br.edu.utfpr.ProjetoIDRAPI.entity.plague;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlagueRepository extends JpaRepository<Plague, Long>, PlagueSpecExecutor {
	Plague findByPlagueName(String name);
}
