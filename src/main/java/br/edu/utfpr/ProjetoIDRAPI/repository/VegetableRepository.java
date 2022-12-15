package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;

public interface VegetableRepository extends JpaRepository<Vegetable, Long>{
	Vegetable findByName(String name);
}
