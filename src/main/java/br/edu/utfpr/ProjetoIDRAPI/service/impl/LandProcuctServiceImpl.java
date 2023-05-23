package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.LandProduct;
import br.edu.utfpr.ProjetoIDRAPI.repository.LandProductRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.LandProductService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandProcuctServiceImpl extends CrudServiceImpl<LandProduct, Long> implements LandProductService {

    private final LandProductRepository productUseRepository;

    public LandProcuctServiceImpl(LandProductRepository productUseRepository) {
        this.productUseRepository = productUseRepository;
    }

    @Override
    protected JpaRepository<LandProduct, Long> getRepository() {
        return this.productUseRepository;
    }

    @Override
    public List<LandProduct> findByPropertyId(Long id) {
        return productUseRepository.findAllByPropertyId(id);
    }
}
