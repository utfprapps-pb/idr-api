package br.edu.utfpr.ProjetoIDRAPI.service;

import br.edu.utfpr.ProjetoIDRAPI.model.PropertyEquipImprove;

import java.util.List;

public interface PropertyEquipImproveService {

    PropertyEquipImprove save(PropertyEquipImprove propertyEquipImprove);

    PropertyEquipImprove findOne(Long id);

    List<PropertyEquipImprove> findAll();

    void delete(Long id);
}
