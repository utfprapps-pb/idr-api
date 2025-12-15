package br.edu.utfpr.ProjetoIDRAPI.entity.medication;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.medication.dto.MedicationDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
