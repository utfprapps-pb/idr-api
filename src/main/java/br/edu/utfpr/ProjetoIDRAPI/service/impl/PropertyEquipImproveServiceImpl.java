package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyEquipImproveRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyEquipImproveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyEquipImproveServiceImpl implements PropertyEquipImproveService {

    private final PropertyEquipImproveRepository repository;

    public PropertyEquipImproveServiceImpl(PropertyEquipImproveRepository repository) {
        this.repository = repository;
    }

    @Override
    public PropertyEquipImprove save(PropertyEquipImprove propertyEquipImprove) {
        return repository.save(propertyEquipImprove);
    }

    @Override
    public PropertyEquipImprove findOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<PropertyEquipImprove> findAll() {
        return repository.findAll();
    }

    @Override
    public List<PropertyEquipImprove> findByPropertyId(Long id) {
        return repository.findAllByPropertyId(id);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
