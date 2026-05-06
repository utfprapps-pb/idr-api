package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralCultivationRepository extends JpaRepository<GeneralCultivation, Long>, GeneralCultivationSpecExecutor {
}
