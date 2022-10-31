package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;

import java.util.List;

public interface PropertyCollaboratorService {

    PropertyCollaborator save(PropertyCollaborator propertyEquipImprove);

    PropertyCollaborator findOne(Long id);

    List<PropertyCollaborator> findAll();

    void delete(Long id);
}
