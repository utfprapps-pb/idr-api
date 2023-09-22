package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.MastitisDto;
import br.edu.utfpr.ProjetoIDRAPI.model.Mastitis;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.MastitisService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/sendMastitis")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse createRegister(@RequestBody @Valid List<Mastitis> mastitisList) {
        mastitisService.saveListMastitis(mastitisList);
        return new GenericResponse("Registros inseridos com sucesso");
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
