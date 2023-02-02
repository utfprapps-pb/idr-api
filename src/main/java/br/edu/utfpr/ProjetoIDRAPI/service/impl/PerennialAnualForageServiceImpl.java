package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualForage;
import br.edu.utfpr.ProjetoIDRAPI.repository.PerennialAnualForageRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PerennialAnualForageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerennialAnualForageServiceImpl implements PerennialAnualForageService {

    private final PerennialAnualForageRepository repository;

    public PerennialAnualForageServiceImpl(PerennialAnualForageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PerennialAnualForage> findByPropertyId(Long id) {
        return repository.findAllByPropertyId(id);
    }

    @Override
    public PerennialAnualForage findOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<PerennialAnualForage> findAll() {
        return repository.findAll();
    }

    @Override
    public PerennialAnualForage save(PerennialAnualForage perennialAnualFodders) {
        return repository.save(perennialAnualFodders);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
