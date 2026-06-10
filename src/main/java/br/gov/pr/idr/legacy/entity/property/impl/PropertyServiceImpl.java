//package br.gov.pr.idr.legacy.entity.property.impl;
//
//import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
//
//import java.util.List;
//
//import br.gov.pr.idr.legacy.entity.property.Property;
//import br.gov.pr.idr.legacy.entity.property.PropertyRepository;
//import br.gov.pr.idr.legacy.entity.property.PropertyService;
//import br.gov.pr.idr.legacy.entity.propertyattachment.PropertyAttachment;
//import br.gov.pr.idr.legacy.entity.propertyattachment.PropertyAttachmentService;
//import br.gov.pr.idr.legacy.entity.user.UserService;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PropertyServiceImpl extends CrudServiceImpl<Property, Long> implements PropertyService {
//
//	private final PropertyRepository propertyRepository;
//    private final PropertyAttachmentService propertyAttachmentService;
//    private final UserService userService;
//
//	public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyAttachmentService propertyAttachmentService, UserService userService) {
//		this.propertyRepository = propertyRepository;
//        this.propertyAttachmentService = propertyAttachmentService;
//        this.userService = userService;
//	}
//
//	@Override
//	protected JpaRepository<Property, Long> getRepository() {
//		return this.propertyRepository;
//	}
//
//	@Override
//	public List<Property> findByProducerId(Long id) {
//		return propertyRepository.findAllByProducerId(id);
//	}
//
//    @Override
//    public List<PropertyAttachment> findAttachmentsById(Long id) {
//        return propertyAttachmentService.findByPropertyId(id);
//    }
//
//    @Override
//    public Property save(Property entity, byte[] attachment) {
//        boolean isNew = (entity.getId() == null || entity.getId() == 0);
//        if (isNew && (entity.getProducer().getId() == null || entity.getProducer().getId() == 0 )) {
//            /*
//                Caso não seja informado um email será gerado um username aleatório, que poderá ser ajustado
//                posteriormente. Para um produtor acessar o sistema o email deverá ser ajustado.
//             */
//            if (entity.getProducer().getUsername() == null) {
//                entity.getProducer().setUsername("agricultor_%d@idrparana.pr.gov.br".formatted(System.currentTimeMillis()));
//                entity.getProducer().setPassword("%d".formatted(System.currentTimeMillis()));
//            }
//            if (entity.getProducer().getCpf() == null) {
//                entity.getProducer().setCpf("%d".formatted(System.currentTimeMillis()));
//            }
//
//            userService.save(entity.getProducer());
//        }
//
//        super.save(entity);
//        handleAttachmentSave(entity, attachment, isNew);
//        return entity;
//    }
//
//    @Override
//    public void delete(Long aLong) {
//        super.delete(aLong);
//        propertyAttachmentService.deleteByPropertyId(aLong);
//    }
//
//    @Override
//	public JpaSpecificationExecutor<Property> getSpecExecutor() {
//		return this.propertyRepository;
//	}
//
//    /**
//     * Login to handle attachment save/delete
//     * This should only be executed while only one attachment is allowed by property
//     */
//    private void handleAttachmentSave(Property entity, byte[] attachment, boolean isNew) {
//        if (attachment != null && attachment.length > 0) {
//            // check if there is already an attachment
//            List<PropertyAttachment> existing = propertyAttachmentService.findByPropertyId(entity.getId());
//
//            if (!existing.isEmpty()) {
//                // update instead of creating a new one
//                PropertyAttachment att = existing.get(0);
//                att.setAttachment(attachment);
//                propertyAttachmentService.save(att);
//            } else {
//                // create new if none exists
//                propertyAttachmentService.save(new PropertyAttachment(entity, attachment));
//            }
//        } else if (!isNew) {
//            // if updating and attachment is null -> delete existing
//            propertyAttachmentService.deleteByPropertyId(entity.getId());
//        }
//    }
//}