package br.gov.pr.idr.legacy.entity.propertyequipimprove;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

import java.util.List;

public interface PropertyEquipImproveService extends CrudService<PropertyEquipImprove, Long> {

    List<PropertyEquipImprove> findByPropertyId(Long id);

}
