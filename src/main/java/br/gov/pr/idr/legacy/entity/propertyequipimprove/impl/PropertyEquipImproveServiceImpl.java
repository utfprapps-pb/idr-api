package br.gov.pr.idr.legacy.entity.propertyequipimprove.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.propertyequipimprove.PropertyEquipImprove;
import br.gov.pr.idr.legacy.entity.propertyequipimprove.PropertyEquipImproveRepository;
import br.gov.pr.idr.legacy.entity.propertyequipimprove.PropertyEquipImproveService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyEquipImproveServiceImpl extends CrudServiceImpl<PropertyEquipImprove, Long>
        implements PropertyEquipImproveService {

    private final PropertyEquipImproveRepository equipImproveRepository;

    public PropertyEquipImproveServiceImpl(PropertyEquipImproveRepository repository) {
        this.equipImproveRepository = repository;
    }

    @Override
    protected JpaRepository<PropertyEquipImprove, Long> getRepository() {
        return this.equipImproveRepository;
    }

    @Override
    public List<PropertyEquipImprove> findByPropertyId(Long id) {
        return null;
//        return equipImproveRepository.findAllByPropertyId(id);
    }

}
