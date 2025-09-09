package br.edu.utfpr.ProjetoIDRAPI.entity.activeprinciple.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.activeprinciple.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.entity.activeprinciple.ActivePrincipleRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.activeprinciple.ActivePrincipleService;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class ActivePrincipleServiceImpl extends CrudServiceImpl<ActivePrinciple, Long> implements ActivePrincipleService {

    private final ActivePrincipleRepository activePrincipleRepository;

    public ActivePrincipleServiceImpl(ActivePrincipleRepository activePrincipleRepository) {
        this.activePrincipleRepository = activePrincipleRepository;
    }

    @Override
    protected JpaRepository<ActivePrinciple, Long> getRepository() {
        return this.activePrincipleRepository;
    }

    @Override
    public JpaSpecificationExecutor<ActivePrinciple> getSpecExecutor() {
        return this.activePrincipleRepository;
    }
}
