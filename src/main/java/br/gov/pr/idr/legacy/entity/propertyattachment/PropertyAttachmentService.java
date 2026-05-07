package br.gov.pr.idr.legacy.entity.propertyattachment;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface PropertyAttachmentService extends CrudService<PropertyAttachment, Long> {
    List<PropertyAttachment> findByPropertyId(Long id);
    void deleteByPropertyId(Long id);
}
