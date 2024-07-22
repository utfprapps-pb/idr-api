package br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyCollaborator.PropertyCollaborator;

import java.util.List;

public interface PropertyCollaboratorService extends CrudService<PropertyCollaborator, Long> {

    List<PropertyCollaborator> findByPropertyId(Long id);

}
