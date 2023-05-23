package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.dto.ProductCategoryDto;
import br.edu.utfpr.ProjetoIDRAPI.model.ProductCategory;
import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.service.ProductCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("productCategories")
public class ProductCategoryController extends CrudController<ProductCategory, ProductCategoryDto, Long> {

    private final ProductCategoryService productCategoryService;
    private ModelMapper modelMapper;

    public ProductCategoryController(ProductCategoryService productCategoryService, ModelMapper modelMapper) {
        super(ProductCategory.class, ProductCategoryDto.class);
        this.productCategoryService = productCategoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    protected CrudService<ProductCategory, Long> getService() {
        return this.productCategoryService;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

}
