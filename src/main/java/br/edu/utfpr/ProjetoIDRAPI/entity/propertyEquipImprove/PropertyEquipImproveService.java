package br.edu.utfpr.ProjetoIDRAPI.entity.propertyEquipImprove;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.propertyEquipImprove.PropertyEquipImprove;

import java.util.List;

public interface PropertyEquipImproveService extends CrudService<PropertyEquipImprove, Long> {

    List<PropertyEquipImprove> findByPropertyId(Long id);

}
