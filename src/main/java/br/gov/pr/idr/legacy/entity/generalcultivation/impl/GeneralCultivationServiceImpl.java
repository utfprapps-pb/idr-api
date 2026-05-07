package br.gov.pr.idr.legacy.entity.generalcultivation.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.generalcultivation.GeneralCultivation;
import br.gov.pr.idr.legacy.entity.generalcultivation.GeneralCultivationRepository;
import br.gov.pr.idr.legacy.entity.generalcultivation.GeneralCultivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralCultivationServiceImpl extends CrudServiceImpl<GeneralCultivation, Long> implements GeneralCultivationService {

    private final GeneralCultivationRepository generalCultivationRepository;

    @Override
    protected JpaRepository<GeneralCultivation, Long> getRepository() {
        return this.generalCultivationRepository;
    }

    @Override
    public JpaSpecificationExecutor<GeneralCultivation> getSpecExecutor() {
        return this.generalCultivationRepository;
    }
}
