package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.ForageDisponibilityService;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageSearchRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
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
		List<ForageDisponibility> list = forageRepository.findByPropertyIdWithProperty(propertyId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		return list.stream()
				.map(f -> {
					ForageDisponibilityDto dto = modelMapper.map(f, ForageDisponibilityDto.class);

					// Converter date para String
					if (f.getDate() != null) {
						dto.setFormation(f.getDate().format(formatter));
					}

					// Converter BigInteger para Long
					if (f.getNumCows() != null) {
						dto.setNumCows(f.getNumCows().longValue());
					}

					// Mapear forage para cultivation
					dto.setCultivation(f.getForage());

					// Mapear picketArea para area, se fizer sentido
					dto.setArea(f.getPicketArea() != null ? f.getPicketArea().toString() : null);

					return dto;
				})
				.toList();
	}


}
