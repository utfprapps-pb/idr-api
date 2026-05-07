package br.gov.pr.idr.legacy.entity.productcategory.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.productcategory.ProductCategory;
import br.gov.pr.idr.legacy.entity.productcategory.ProductCategoryRepository;
import br.gov.pr.idr.legacy.entity.productcategory.ProductCategoryService;
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
