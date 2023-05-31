package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalDiseases;

public interface AnimalDiseasesRepository extends JpaRepository<AnimalDiseases, Long> {

}
