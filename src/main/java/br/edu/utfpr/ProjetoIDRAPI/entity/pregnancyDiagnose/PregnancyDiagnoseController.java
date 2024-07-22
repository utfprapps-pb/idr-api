package br.edu.utfpr.ProjetoIDRAPI.entity.pregnancyDiagnose;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.pregnancyDiagnose.dto.PregnancyDiagnoseDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
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
