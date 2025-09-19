package br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface PropertyAttachmentService extends CrudService<PropertyAttachment, Long> {
    List<PropertyAttachment> findByPropertyId(Long id);
    void deleteByPropertyId(Long id);
}
