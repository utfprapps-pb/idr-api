package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.PerennialAnualFodders;
import br.edu.utfpr.ProjetoIDRAPI.repository.PerennialAnualFoddersRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PerennialAnualFoddersService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerennialAnualFoddersServiceImpl implements PerennialAnualFoddersService {

    private final PerennialAnualFoddersRepository repository;

    public PerennialAnualFoddersServiceImpl(PerennialAnualFoddersRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PerennialAnualFodders> findByPropertyId(Long id) {
        return repository.findAllByPropertyId(id);
    }

    @Override
    public PerennialAnualFodders findOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<PerennialAnualFodders> findAll() {
        return repository.findAll();
    }

    @Override
    public PerennialAnualFodders save(PerennialAnualFodders perennialAnualFodders) {
        return repository.save(perennialAnualFodders);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
