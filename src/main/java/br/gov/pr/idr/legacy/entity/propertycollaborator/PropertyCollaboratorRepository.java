package br.gov.pr.idr.legacy.entity.propertycollaborator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyCollaboratorRepository extends JpaRepository<PropertyCollaborator, Long> {
	//método que irá buscar os colaboradores pelo id da propriedade
    List<PropertyCollaborator> findAllByPropertyId(Long id);

}
