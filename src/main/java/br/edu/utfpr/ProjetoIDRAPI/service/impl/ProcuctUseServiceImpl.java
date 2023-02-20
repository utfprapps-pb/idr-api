package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.ProductUse;
import br.edu.utfpr.ProjetoIDRAPI.repository.ProductUseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ProductUseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcuctUseServiceImpl extends CrudServiceImpl<ProductUse, Long> implements ProductUseService {

    private final ProductUseRepository productUseRepository;

    public ProcuctUseServiceImpl(ProductUseRepository productUseRepository) {
        this.productUseRepository = productUseRepository;
    }

    @Override
    protected JpaRepository<ProductUse, Long> getRepository() {
        return this.productUseRepository;
    }

    @Override
    public List<ProductUse> findByPropertyId(Long id) {
        return productUseRepository.findAllByPropertyId(id);
    }
}
