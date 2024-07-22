package br.edu.utfpr.ProjetoIDRAPI.entity.vegetableDisease;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.entity.vegetableDisease.VegetableDisease;

public interface VegetableDiseaseRepository extends JpaRepository<VegetableDisease, Long>{
	
}
