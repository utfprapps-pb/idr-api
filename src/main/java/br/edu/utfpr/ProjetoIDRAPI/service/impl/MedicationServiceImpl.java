package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.Medication;
import br.edu.utfpr.ProjetoIDRAPI.repository.MedicationRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.MedicationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicationServiceImpl extends CrudServiceImpl<Medication, Long> implements MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    protected JpaRepository<Medication, Long> getRepository() {
        return this.medicationRepository;
    }

}
