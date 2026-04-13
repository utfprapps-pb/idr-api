package br.edu.utfpr.ProjetoIDRAPI.Test.Controller;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudControllerTest;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.Medication;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.MedicationRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.dto.MedicationDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class MedicationControllerTest extends CrudControllerTest<Medication, MedicationDto, Long> {

    @Autowired
    private MedicationRepository medicationRepository;

    @Override
    protected Long persistAndReturnId(Medication entity) {
        return medicationRepository.save(entity).getId();
    }

    @Override
    protected void cleanUpDatabase() {
        medicationRepository.deleteAll();
    }

    @Override
    protected Medication createValidObject() {
        return TestUtils.createValidMedication();
    }

    @Override
    protected Medication createInvalidObject() {
        return new Medication();
    }

    @Override
    protected String getURL() {
        return "/medications";
    }

    @Override
    protected Class<MedicationDto> getDtoClass() {
        return MedicationDto.class;
    }
}