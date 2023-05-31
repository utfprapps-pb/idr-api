package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.PregnancyDiagnose;
import br.edu.utfpr.ProjetoIDRAPI.repository.PregnancyDiagnoseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PregnancyDiagnoseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PregnancyDiagnoseServiceImpl extends CrudServiceImpl<PregnancyDiagnose, Long> implements PregnancyDiagnoseService {

    private final PregnancyDiagnoseRepository diagnoseRepository;

    public PregnancyDiagnoseServiceImpl(PregnancyDiagnoseRepository diagnoseRepository) {
        this.diagnoseRepository = diagnoseRepository;
    }

    @Override
    protected JpaRepository<PregnancyDiagnose, Long> getRepository() {
        return this.diagnoseRepository;
    }

}
