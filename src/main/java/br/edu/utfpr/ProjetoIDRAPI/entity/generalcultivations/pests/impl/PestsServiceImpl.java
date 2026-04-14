package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests.Pests;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests.PestsRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests.PestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PestsServiceImpl extends CrudServiceImpl<Pests, Long> implements PestsService {

    private final PestsRepository pestsRepository;

    @Override
    protected JpaRepository<Pests, Long> getRepository() {
        return this.pestsRepository;
    }

    @Override
    public JpaSpecificationExecutor<Pests> getSpecExecutor() {
        return this.pestsRepository;
    }
}
