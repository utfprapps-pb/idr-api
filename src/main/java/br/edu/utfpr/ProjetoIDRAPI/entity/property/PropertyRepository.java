package br.edu.utfpr.ProjetoIDRAPI.entity.property;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>, PropertySpecExecutor {
	//método que irá buscar as propriedades pelo id do usuario
    List<Property> findAllByUserId(Long id);
}
