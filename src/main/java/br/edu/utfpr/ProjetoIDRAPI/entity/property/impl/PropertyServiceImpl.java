package br.edu.utfpr.ProjetoIDRAPI.entity.property.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyService;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachment;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachmentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl extends CrudServiceImpl<Property, Long> implements PropertyService {

	private final PropertyRepository propertyRepository;
    private final PropertyAttachmentService propertyAttachmentService;

	public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyAttachmentService propertyAttachmentService) {
		this.propertyRepository = propertyRepository;
        this.propertyAttachmentService = propertyAttachmentService;
	}

	@Override
	protected JpaRepository<Property, Long> getRepository() {
		return this.propertyRepository;
	}

	@Override
	public List<Property> findByUserId(Long id) {
		return propertyRepository.findAllByUserId(id);
	}

    @Override
    public List<PropertyAttachment> findAttachmentsById(Long id) {
        return propertyAttachmentService.findByPropertyId(id);
    }

    @Override
    public Property save(Property entity, byte[] attachment) {
        super.save(entity);
        propertyAttachmentService.save(new PropertyAttachment(entity, attachment));
        return entity;
    }

    @Override
	public JpaSpecificationExecutor<Property> getSpecExecutor() {
		return this.propertyRepository;
	}
}