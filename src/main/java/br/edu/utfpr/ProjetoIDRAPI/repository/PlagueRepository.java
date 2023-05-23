package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Plague;

public interface PlagueRepository extends JpaRepository<Plague, Long> {
	Plague findByPlagueName(String name);
}
