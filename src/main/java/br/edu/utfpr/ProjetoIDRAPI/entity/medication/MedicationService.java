package br.edu.utfpr.ProjetoIDRAPI.entity.medication;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.Medication;

import java.util.List;

public interface MedicationService extends CrudService<Medication, Long> {
    boolean saveListMedications(List<Medication> medications);

}
