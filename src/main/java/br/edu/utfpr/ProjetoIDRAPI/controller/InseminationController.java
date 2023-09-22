package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.InseminationDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Insemination;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.InseminationService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("inseminations")
public class InseminationController extends CrudController<Insemination, InseminationDto, Long> {

    private final InseminationService inseminationService;
    private ModelMapper modelMapper;

    public InseminationController(InseminationService inseminationService, ModelMapper modelMapper) {
        super(Insemination.class, InseminationDto.class);
        this.inseminationService = inseminationService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<Insemination, Long> getService() {
        return this.inseminationService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @PostMapping("/sendInseminations")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid List<Insemination> inseminations) {
        inseminationService.saveListInseminations(inseminations);
        return new GenericResponse("Registros inseridos com sucesso");
    }
}
