package br.edu.utfpr.ProjetoIDRAPI.entity.breed;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Long>, BreedSpecExecutor {
}
