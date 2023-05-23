package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.repository.ActivePrincipleRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ActivePrincipleService;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
