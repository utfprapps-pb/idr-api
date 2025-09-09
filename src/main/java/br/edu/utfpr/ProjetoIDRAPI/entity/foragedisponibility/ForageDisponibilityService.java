package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;

import java.util.List;

public interface ForageDisponibilityService extends CrudService<ForageDisponibility, Long> {
    List<ForageDisponibilityDto> findByPropertyId(Long propertyId);

}
