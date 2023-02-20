package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;

import java.util.List;

public interface PropertyEquipImproveService extends CrudService<PropertyEquipImprove, Long>{

    List<PropertyEquipImprove> findByPropertyId(Long id);

}
