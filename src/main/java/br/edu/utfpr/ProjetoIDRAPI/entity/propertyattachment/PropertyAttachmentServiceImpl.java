package br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyAttachmentServiceImpl extends CrudServiceImpl<PropertyAttachment, Long> implements PropertyAttachmentService {

    private final PropertyAttachmentRepository repository;

    public PropertyAttachmentServiceImpl(PropertyAttachmentRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<PropertyAttachment, Long> getRepository() {
        return repository;
    }

    @Override
    @Transactional // required for loading lazy byte[]
    public PropertyAttachment findOne(Long aLong) {
        return super.findOne(aLong);
    }

    @Override
    @Transactional // required for loading lazy byte[]
    public List<PropertyAttachment> findByPropertyId(Long id) {
        return repository.findByPropertyId(id);
    }

    @Override
    public void deleteByPropertyId(Long id) {
        repository.deleteByPropertyId(id);
    }
}
