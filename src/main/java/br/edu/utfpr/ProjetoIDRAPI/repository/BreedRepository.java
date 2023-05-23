package br.edu.utfpr.ProjetoIDRAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.model.Breed;

public interface BreedRepository extends JpaRepository<Breed, Long> {
}
