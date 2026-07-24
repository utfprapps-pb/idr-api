//package br.gov.pr.idr.legacy.entity.propertycollaborator.impl;
//
//import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
//import br.gov.pr.idr.legacy.entity.propertycollaborator.PropertyCollaborator;
//import br.gov.pr.idr.legacy.entity.propertycollaborator.PropertyCollaboratorRepository;
//import br.gov.pr.idr.legacy.entity.propertycollaborator.PropertyCollaboratorService;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PropertyCollaboratorServiceImpl extends CrudServiceImpl<PropertyCollaborator, Long>
//        implements PropertyCollaboratorService {
//
//    private final PropertyCollaboratorRepository propertyCollaboratorRepository;
//
//    public PropertyCollaboratorServiceImpl(PropertyCollaboratorRepository repository) {
//        this.propertyCollaboratorRepository = repository;
//    }
//
//    @Override
//    protected JpaRepository<PropertyCollaborator, Long> getRepository() {
//        return this.propertyCollaboratorRepository;
//    }
//
//    @Override
//    public List<PropertyCollaborator> findByPropertyId(Long id) {
//        return propertyCollaboratorRepository.findAllByPropertyId(id);
//    }
//
//}
