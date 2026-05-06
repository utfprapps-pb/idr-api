package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.GeneralCultivation;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.GeneralCultivationRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.GeneralCultivationService;
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
