package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.MastitisDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Mastitis;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.MastitisService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mastitis")
public class MastitisController extends CrudController<Mastitis, MastitisDto, Long> {

    private final MastitisService mastitisService;
    private ModelMapper modelMapper;

    public MastitisController(MastitisService mastitisService, ModelMapper modelMapper) {
        super(Mastitis.class, MastitisDto.class);
        this.mastitisService = mastitisService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<Mastitis, Long> getService() {
        return this.mastitisService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
