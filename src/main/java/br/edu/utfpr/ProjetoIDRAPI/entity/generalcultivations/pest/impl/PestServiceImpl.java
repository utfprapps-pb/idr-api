package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pest.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pest.Pest;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pest.PestRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pest.PestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PestServiceImpl extends CrudServiceImpl<Pest, Long> implements PestService {

    private final PestRepository pestRepository;

    @Override
    protected JpaRepository<Pest, Long> getRepository() {
        return this.pestRepository;
    }

    @Override
    public JpaSpecificationExecutor<Pest> getSpecExecutor() {
        return this.pestRepository;
    }
}
