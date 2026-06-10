package br.gov.pr.idr.legacy.entity.perennialanualforage.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.perennialanualforage.PerennialAnualForage;
import br.gov.pr.idr.legacy.entity.perennialanualforage.PerennialAnualForageRepository;
import br.gov.pr.idr.legacy.entity.perennialanualforage.PerennialAnualForageService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerennialAnualForageServiceImpl extends CrudServiceImpl<PerennialAnualForage, Long>
        implements PerennialAnualForageService {

    private final PerennialAnualForageRepository perennialAnualForageRepository;

    public PerennialAnualForageServiceImpl(PerennialAnualForageRepository repository) {
        this.perennialAnualForageRepository = repository;
    }

    @Override
    public List<PerennialAnualForage> findByPropertyId(Long id) {
        return null;
//        return perennialAnualForageRepository.findAllByPropertyId(id);
    }

    @Override
    protected JpaRepository<PerennialAnualForage, Long> getRepository() {
        return this.perennialAnualForageRepository;
    }

}
