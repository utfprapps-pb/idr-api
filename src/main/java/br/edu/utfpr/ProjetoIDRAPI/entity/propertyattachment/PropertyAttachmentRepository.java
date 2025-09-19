package br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyAttachmentRepository extends JpaRepository<PropertyAttachment, Long> {

    List<PropertyAttachment> findByPropertyId(Long id);
}
