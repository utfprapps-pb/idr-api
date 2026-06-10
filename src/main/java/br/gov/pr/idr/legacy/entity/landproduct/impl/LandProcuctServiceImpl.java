package br.gov.pr.idr.legacy.entity.landproduct.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.landproduct.LandProduct;
import br.gov.pr.idr.legacy.entity.landproduct.LandProductRepository;
import br.gov.pr.idr.legacy.entity.landproduct.LandProductService;
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
        return null;
//        return productUseRepository.findAllByPropertyId(id);
    }
}
