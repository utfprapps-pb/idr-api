package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyCollaboratorRepository extends JpaRepository<PropertyCollaborator, Long> {
	//método que irá buscar os colaboradores pelo id da propriedade
    List<PropertyCollaborator> findAllByPropertyId(Long id);

}
