package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.repository.PropertyCollaboratorRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PropertyCollaboratorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyCollaboratorServiceImpl implements PropertyCollaboratorService {

    private final PropertyCollaboratorRepository repository;

    public PropertyCollaboratorServiceImpl(PropertyCollaboratorRepository repository) {
        this.repository = repository;
    }

    @Override
    public PropertyCollaborator save(PropertyCollaborator propertyEquipImprove) {
        return repository.save(propertyEquipImprove);
    }

    @Override
    public PropertyCollaborator findOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<PropertyCollaborator> findAll() {
        return repository.findAll();
    }

    @Override
    public List<PropertyCollaborator> findByPropertyId(Long id) {
        return repository.findAllByPropertyId(id);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
