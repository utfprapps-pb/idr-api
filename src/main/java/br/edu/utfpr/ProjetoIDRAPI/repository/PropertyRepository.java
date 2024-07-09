package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.Property;
import br.edu.utfpr.ProjetoIDRAPI.specexecutor.PropertySpecExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>, PropertySpecExecutor {
	//método que irá buscar as propriedades pelo id do usuario
    List<Property> findAllByUserId(Long id);
}
