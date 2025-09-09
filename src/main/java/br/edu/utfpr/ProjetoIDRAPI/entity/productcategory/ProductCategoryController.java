package br.edu.utfpr.ProjetoIDRAPI.entity.productcategory;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudController;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.productcategory.dto.ProductCategoryDto;
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
