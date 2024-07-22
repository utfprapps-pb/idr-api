package br.edu.utfpr.ProjetoIDRAPI.entity.activePrinciple;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivePrincipleRepository extends JpaRepository<ActivePrinciple, Long>, ActivePrincipleSpecExecutor {
}
