package br.edu.utfpr.ProjetoIDRAPI.entity.property.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyService;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachment;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment.PropertyAttachmentService;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl extends CrudServiceImpl<Property, Long> implements PropertyService {

	private final PropertyRepository propertyRepository;
    private final PropertyAttachmentService propertyAttachmentService;
    private final UserService userService;

	public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyAttachmentService propertyAttachmentService, UserService userService) {
		this.propertyRepository = propertyRepository;
        this.propertyAttachmentService = propertyAttachmentService;
        this.userService = userService;
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
        boolean isNew = (entity.getId() == 0);
        entity.setUser(userService.findSelfUser());
        super.save(entity);
        handleAttachmentSave(entity, attachment, isNew);
        return entity;
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
        propertyAttachmentService.deleteByPropertyId(aLong);
    }

    @Override
	public JpaSpecificationExecutor<Property> getSpecExecutor() {
		return this.propertyRepository;
	}

    /**
     * Login to handle attachment save/delete
     * This should only be executed while only one attachment is allowed by property
     */
    private void handleAttachmentSave(Property entity, byte[] attachment, boolean isNew) {
        if (attachment != null) {
            // check if there is already an attachment
            List<PropertyAttachment> existing = propertyAttachmentService.findByPropertyId(entity.getId());

            if (!existing.isEmpty()) {
                // update instead of creating a new one
                PropertyAttachment att = existing.get(0);
                att.setAttachment(attachment);
                propertyAttachmentService.save(att);
            } else {
                // create new if none exists
                propertyAttachmentService.save(new PropertyAttachment(entity, attachment));
            }
        } else if (!isNew) {
            // if updating and attachment is null -> delete existing
            propertyAttachmentService.deleteByPropertyId(entity.getId());
        }
    }
}