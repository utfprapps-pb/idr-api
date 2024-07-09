package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductCategory;
import br.edu.utfpr.ProjetoIDRAPI.repository.ProductCategoryRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ProductCategoryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl extends CrudServiceImpl<ProductCategory, Long> implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    protected JpaRepository<ProductCategory, Long> getRepository() {
        return this.productCategoryRepository;
    }

    @Override
    public JpaSpecificationExecutor<ProductCategory> getSpecExecutor() {
        return this.productCategoryRepository;
    }
}
