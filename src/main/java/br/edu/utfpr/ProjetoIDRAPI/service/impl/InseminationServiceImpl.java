package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Insemination;
import br.edu.utfpr.ProjetoIDRAPI.repository.InseminationRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.InseminationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class InseminationServiceImpl extends CrudServiceImpl<Insemination, Long> implements InseminationService {

    private final InseminationRepository inseminationRepository;

    public InseminationServiceImpl(InseminationRepository inseminationRepository) {
        this.inseminationRepository = inseminationRepository;
    }

    @Override
    protected JpaRepository<Insemination, Long> getRepository() {
        return this.inseminationRepository;
    }

}
