package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.ProductUseDto;
import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.ProductUseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("productsUse")
public class ProductUseController extends CrudController<ProductUse, ProductUseDto, Long> {

    private final ProductUseService productUseService;
    private ModelMapper modelMapper;

    public ProductUseController(ProductUseService productUseService, ModelMapper modelMapper) {
        super(ProductUse.class, ProductUseDto.class);
        this.productUseService = productUseService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<ProductUse, Long> getService() {
        return this.productUseService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @GetMapping("/productUseProp/{id}")
    public ResponseEntity<List<ProductUseDto>> findAllByPropertyId(@PathVariable Long id) {
        return ResponseEntity.ok(
                productUseService.findByPropertyId(id)
                        .stream()
                        .map(this::convertEntityToDto)
                        .collect(Collectors.toList())
        );
    }

    private ProductUseDto convertEntityToDto(ProductUse productUse) {
        return modelMapper.map(productUse, ProductUseDto.class);
    }
}
