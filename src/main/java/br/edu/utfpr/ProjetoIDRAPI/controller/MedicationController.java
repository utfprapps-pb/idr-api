package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.MedicationDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Medication;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.MedicationService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PostMapping("/sendMedications")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid List<Medication> medicationsList) {
        medicationService.saveListMedications(medicationsList);
        return new GenericResponse("Registros inseridos com sucesso");
    }
}
