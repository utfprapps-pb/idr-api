package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.AnimalPurchases;

public interface AnimalPurchasesRepository extends JpaRepository<AnimalPurchases, Long> {

}
