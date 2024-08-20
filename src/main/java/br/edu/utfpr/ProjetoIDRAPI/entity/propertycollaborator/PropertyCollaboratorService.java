package br.edu.utfpr.ProjetoIDRAPI.entity.propertycollaborator;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface PropertyCollaboratorService extends CrudService<PropertyCollaborator, Long> {

    List<PropertyCollaborator> findByPropertyId(Long id);

}
