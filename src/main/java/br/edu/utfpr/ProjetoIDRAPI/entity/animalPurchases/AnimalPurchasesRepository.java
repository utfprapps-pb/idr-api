package br.edu.utfpr.ProjetoIDRAPI.entity.animalPurchases;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.entity.animalPurchases.AnimalPurchases;

public interface AnimalPurchasesRepository extends JpaRepository<AnimalPurchases, Long> {

}
