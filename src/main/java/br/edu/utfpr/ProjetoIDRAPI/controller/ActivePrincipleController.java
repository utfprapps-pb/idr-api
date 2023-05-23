package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.ActivePrincipleDto;
import br.edu.utfpr.ProjetoIDRAPI.model.ActivePrinciple;
import br.edu.utfpr.ProjetoIDRAPI.service.ActivePrincipleService;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("activePrinciples")
public class ActivePrincipleController extends CrudController<ActivePrinciple, ActivePrincipleDto, Long> {

    private final ActivePrincipleService activePrincipleService;
    private ModelMapper modelMapper;

    public ActivePrincipleController(ActivePrincipleService activePrincipleService, ModelMapper modelMapper) {
        super(ActivePrinciple.class, ActivePrincipleDto.class);
        this.activePrincipleService = activePrincipleService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<ActivePrinciple, Long> getService() {
        return this.activePrincipleService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
