package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.repository.ProductUseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ProductUseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcuctUseServiceImpl implements ProductUseService {

    private final ProductUseRepository productUseRepository;

    public ProcuctUseServiceImpl(ProductUseRepository productUseRepository) {
        this.productUseRepository = productUseRepository;
    }

    @Override
    public ProductUse findOne(Long id) {
        return productUseRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductUse> findAll() {
        return productUseRepository.findAll();
    }

    @Override
    public ProductUse save(ProductUse productUse) {
        return productUseRepository.save(productUse);
    }

    @Override
    public void delete(Long id) {
        productUseRepository.deleteById(id);
    }

    @Override
    public List<ProductUse> findByPropertyId(Long id) {
        return productUseRepository.findAllByPropertyId(id);
    }
}
