package br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyEquipImproveRepository extends JpaRepository<PropertyEquipImprove, Long> {
	//método que irá buscar os equipamentos pelo id da propriedade
    List<PropertyEquipImprove> findAllByPropertyId(Long id);
}
