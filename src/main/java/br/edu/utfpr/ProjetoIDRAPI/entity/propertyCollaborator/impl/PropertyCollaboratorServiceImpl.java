package br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.PropertyCollaborator;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.PropertyCollaboratorRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.PropertyCollaboratorService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyCollaboratorServiceImpl extends CrudServiceImpl<PropertyCollaborator, Long>
        implements PropertyCollaboratorService {

    private final PropertyCollaboratorRepository propertyCollaboratorRepository;

    public PropertyCollaboratorServiceImpl(PropertyCollaboratorRepository repository) {
        this.propertyCollaboratorRepository = repository;
    }

    @Override
    protected JpaRepository<PropertyCollaborator, Long> getRepository() {
        return this.propertyCollaboratorRepository;
    }

    @Override
    public List<PropertyCollaborator> findByPropertyId(Long id) {
        return propertyCollaboratorRepository.findAllByPropertyId(id);
    }

}
