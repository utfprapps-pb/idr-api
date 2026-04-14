package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PestsRepository extends JpaRepository<Pests, Long>, PestsSpecExecutor {
}
