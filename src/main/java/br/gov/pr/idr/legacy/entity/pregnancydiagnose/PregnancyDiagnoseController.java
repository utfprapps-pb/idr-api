package br.gov.pr.idr.legacy.entity.pregnancydiagnose;

import br.gov.pr.idr.legacy.entity.crud.CrudController;
import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.pregnancydiagnose.dto.PregnancyDiagnoseDto;
import br.gov.pr.idr.legacy.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pregnancyDiagnose")
public class PregnancyDiagnoseController extends CrudController<PregnancyDiagnose, PregnancyDiagnoseDto, Long> {

    private final PregnancyDiagnoseService diagnoseService;
    private ModelMapper modelMapper;

    public PregnancyDiagnoseController(PregnancyDiagnoseService diagnoseService, ModelMapper modelMapper) {
        super(PregnancyDiagnose.class, PregnancyDiagnoseDto.class);
        this.diagnoseService = diagnoseService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<PregnancyDiagnose, Long> getService() {
        return this.diagnoseService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @PostMapping("/sendPregnancyDiagnoses")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid List<PregnancyDiagnose> pregnancyDiagnoseList) {
        diagnoseService.saveListPregnancyDiagnoses(pregnancyDiagnoseList);
        return new GenericResponse("Registros inseridos com sucesso");
    }
}
