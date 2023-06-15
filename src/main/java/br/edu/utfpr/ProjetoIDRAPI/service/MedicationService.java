package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.Medication;

import java.util.List;

public interface MedicationService extends CrudService<Medication, Long> {
    boolean saveListMedications(List<Medication> medications);

}
