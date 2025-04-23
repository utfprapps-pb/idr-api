package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.Medication;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.dto.MedicationDto;

public class MedicationControllerTest extends CrudControllerTest<Medication, MedicationDto, Long> {

    @Override
    protected Medication createValidObject() {
        return Medication.builder()
                .appliedDose("Test")
                .applicationWay("Test")
                .build();
    }

    @Override
    protected Medication createInvalidObject() { return Medication.builder().build(); }

    @Override
    protected Long getValidId() { return 1L; }

    @Override
    protected String getURL() { return "/medications"; }
}
