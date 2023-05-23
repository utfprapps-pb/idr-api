package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;

public interface VegetableDiseaseRepository extends JpaRepository<VegetableDisease, Long>{
	
}
