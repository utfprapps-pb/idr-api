package br.gov.pr.idr.legacy.entity.foragedisponibility.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.foragedisponibility.ForageDisponibility;
import br.gov.pr.idr.legacy.entity.foragedisponibility.ForageDisponibilityRepository;
import br.gov.pr.idr.legacy.entity.foragedisponibility.ForageDisponibilityService;
import br.gov.pr.idr.legacy.entity.foragedisponibility.dto.ForageDisponibilityDto;
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
		List<ForageDisponibility> list = null;
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
