package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.LandProductDto;
import br.edu.utfpr.ProjetoIDRAPI.model.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.LandProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("landProducts")
public class LandProductController extends CrudController<LandProduct, LandProductDto, Long> {

    private final LandProductService productUseService;
    private ModelMapper modelMapper;

    public LandProductController(LandProductService productUseService, ModelMapper modelMapper) {
        super(LandProduct.class, LandProductDto.class);
        this.productUseService = productUseService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<LandProduct, Long> getService() {
        return this.productUseService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @GetMapping("/productUseProp/{id}")
    public ResponseEntity<List<LandProductDto>> findAllByPropertyId(@PathVariable Long id) {
        return ResponseEntity.ok(
                productUseService.findByPropertyId(id)
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
    }

    private LandProductDto convertEntityToDto(LandProduct productUse) {
        return modelMapper.map(productUse, LandProductDto.class);
    }
}
