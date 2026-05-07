package br.gov.pr.idr.legacy.entity.medication;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface MedicationService extends CrudService<Medication, Long> {
    boolean saveListMedications(List<Medication> medications);

}
