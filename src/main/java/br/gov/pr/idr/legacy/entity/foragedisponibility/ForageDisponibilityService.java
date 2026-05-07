package br.gov.pr.idr.legacy.entity.foragedisponibility;

import br.gov.pr.idr.legacy.entity.crud.CrudService;
import br.gov.pr.idr.legacy.entity.foragedisponibility.dto.ForageDisponibilityDto;

import java.util.List;

public interface ForageDisponibilityService extends CrudService<ForageDisponibility, Long> {
    List<ForageDisponibilityDto> findByPropertyId(Long propertyId);


}
