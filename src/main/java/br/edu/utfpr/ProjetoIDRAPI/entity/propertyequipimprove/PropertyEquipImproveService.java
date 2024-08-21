package br.edu.utfpr.ProjetoIDRAPI.entity.propertyequipimprove;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;

import java.util.List;

public interface PropertyEquipImproveService extends CrudService<PropertyEquipImprove, Long> {

    List<PropertyEquipImprove> findByPropertyId(Long id);

}
