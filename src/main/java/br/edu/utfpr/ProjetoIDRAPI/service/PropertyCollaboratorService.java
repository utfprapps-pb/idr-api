package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;

import java.util.List;

public interface PropertyCollaboratorService extends CrudService<PropertyCollaborator, Long> {
    List<PropertyCollaborator> findByPropertyId(Long id);
}
