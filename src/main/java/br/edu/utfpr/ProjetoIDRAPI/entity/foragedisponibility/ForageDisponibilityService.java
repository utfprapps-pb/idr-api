package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageCreateDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageUpdateDto;

import java.util.List;

public interface ForageDisponibilityService extends CrudService<ForageDisponibility, Long> {
    List<ForageDisponibilityDto> findByPropertyId(Long propertyId);
    ForageDisponibility createForage(Long propertyId, ForageCreateDto createDto);
    void updateForage(Long propertyId, Long forageId, ForageUpdateDto updateDto);
    ForageDisponibilityDto findDtoById(Long id);
}
