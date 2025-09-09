package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibilityService;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ForageDisponibilityServiceImpl extends CrudServiceImpl<ForageDisponibility, Long>
		implements ForageDisponibilityService {

	private final ForageDisponibilityRepository forageRepository;
	private final ModelMapper modelMapper;

	public ForageDisponibilityServiceImpl(ForageDisponibilityRepository forageRepository, ModelMapper modelMapper) {
		this.forageRepository = forageRepository;
        this.modelMapper = modelMapper;
    }

	@Override
	protected JpaRepository<ForageDisponibility, Long> getRepository() {
		return this.forageRepository;
	}

	@Override
	public List<ForageDisponibilityDto> findByPropertyId(Long propertyId) {

		System.out.println("Buscando por propertyId: " + propertyId);List<ForageDisponibility> list = forageRepository.findByPropertyIdWithProperty(propertyId);
		return list.stream()
				.map(f -> modelMapper.map(f, ForageDisponibilityDto.class))
				.toList();
	}

}
