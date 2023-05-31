package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.PregnancyDiagnoseDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PregnancyDiagnose;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PregnancyDiagnoseService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
