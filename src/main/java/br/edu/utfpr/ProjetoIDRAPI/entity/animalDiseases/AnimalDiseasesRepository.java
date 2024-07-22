package br.edu.utfpr.ProjetoIDRAPI.entity.animalDiseases;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.entity.animalDiseases.AnimalDiseases;

public interface AnimalDiseasesRepository extends JpaRepository<AnimalDiseases, Long> {

}
