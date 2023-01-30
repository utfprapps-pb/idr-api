package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.PerennialAnualFoddersDto;
import br.edu.utfpr.ProjetoIDRAPI.dto.PropertyCollaboratorDto;
import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualFodders;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.PerennialAnualFoddersService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fodders")
public class PerennialAnualFoddersController extends CrudController<PerennialAnualFodders, PerennialAnualFoddersDto, Long> {

    private final PerennialAnualFoddersService perennialAnualFoddersService;
    private ModelMapper modelMapper;

    public PerennialAnualFoddersController(PerennialAnualFoddersService perennialAnualFoddersService, ModelMapper modelMapper) {
        super(PerennialAnualFodders.class, PerennialAnualFoddersDto.class);
        this.perennialAnualFoddersService = perennialAnualFoddersService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<PerennialAnualFodders, Long> getService() {
        return this.perennialAnualFoddersService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @GetMapping("/fodderProperty/{id}")
    public ResponseEntity<List<PerennialAnualFoddersDto>> findAllByPropertyId(@PathVariable Long id) {
        return ResponseEntity.ok(
                perennialAnualFoddersService.findByPropertyId(id)
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
    }

    private PerennialAnualFoddersDto convertEntityToDto(PerennialAnualFodders perennialAnualFodders) {
        return modelMapper.map(perennialAnualFodders, PerennialAnualFoddersDto.class);
    }
}
