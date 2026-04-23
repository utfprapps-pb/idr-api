package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PestRepository extends JpaRepository<Pest, Long>, PestSpecExecutor {
}
