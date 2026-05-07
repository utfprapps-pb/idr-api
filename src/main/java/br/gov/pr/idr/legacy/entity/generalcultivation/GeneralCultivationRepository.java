package br.gov.pr.idr.legacy.entity.generalcultivation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralCultivationRepository extends JpaRepository<GeneralCultivation, Long>, GeneralCultivationSpecExecutor {
}
