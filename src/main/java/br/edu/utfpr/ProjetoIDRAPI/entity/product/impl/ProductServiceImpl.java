package br.edu.utfpr.ProjetoIDRAPI.entity.product.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.Product;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.ProductRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends CrudServiceImpl<Product, Long> implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return this.productRepository;
    }

    @Override
    public JpaSpecificationExecutor<Product> getSpecExecutor() {
        return this.productRepository;
    }
}
