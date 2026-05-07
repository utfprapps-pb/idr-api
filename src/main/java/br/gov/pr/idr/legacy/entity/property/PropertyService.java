package br.gov.pr.idr.legacy.entity.property;

import java.util.List;

import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.propertyattachment.PropertyAttachment;

public interface PropertyService extends CrudService<Property, Long> {

	List<Property> findByProducerId(Long id);
    List<PropertyAttachment> findAttachmentsById(Long id);
    Property save(Property entity, byte[] attachment);
}
