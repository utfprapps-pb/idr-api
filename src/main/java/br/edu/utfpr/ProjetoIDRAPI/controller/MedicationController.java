package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.MedicationDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Medication;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.MedicationService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("medications")
public class MedicationController extends CrudController<Medication, MedicationDto, Long> {

    private final MedicationService medicationService;
    private ModelMapper modelMapper;

    public MedicationController(MedicationService medicationService, ModelMapper modelMapper) {
        super(Medication.class, MedicationDto.class);
        this.medicationService = medicationService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<Medication, Long> getService() {
        return this.medicationService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
