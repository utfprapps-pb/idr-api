package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.*;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageCreateDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageDisponibilityDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility.dto.ForageUpdateDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.PropertyRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class ForageDisponibilityServiceImpl extends CrudServiceImpl<ForageDisponibility, Long>
		implements ForageDisponibilityService {

	private final ForageDisponibilityRepository forageRepository;
	private final PropertyRepository propertyRepository;
	private final ModelMapper modelMapper;
	private final EntityManager entityManager;
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE_TIME;

	public ForageDisponibilityServiceImpl(ForageDisponibilityRepository forageRepository,
										  PropertyRepository propertyRepository,
										  ModelMapper modelMapper,
										  EntityManager entityManager) {
		this.forageRepository = forageRepository;
		this.propertyRepository = propertyRepository;
		this.modelMapper = modelMapper;
		this.entityManager = entityManager;
	}

	@Override
	protected JpaRepository<ForageDisponibility, Long> getRepository() {
		return this.forageRepository;
	}

	@Override
	public ForageDisponibility createForage(Long propertyId, ForageCreateDto createDto) {
		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Propriedade não encontrada com ID: " + propertyId));
		ForageDisponibility forage = modelMapper.map(createDto, ForageDisponibility.class);

		forage.setForage(createDto.getCultivation());

		forage.setPicketArea(createDto.getArea());

		try {
			LocalDate date = LocalDate.parse(createDto.getFormation().substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
			forage.setDate(date);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de data inválido para 'formation'. Use YYYY-MM-DD.");
		}
		if (createDto.getNumCows() != null) {
			forage.setNumCows(BigDecimal.valueOf(createDto.getNumCows()));
		}
		forage.setProperty(property);
		return forageRepository.save(forage);
	}

	@Override
	public List<ForageDisponibilityDto> findByPropertyId(Long propertyId) {
		List<ForageDisponibility> list = forageRepository.findByPropertyIdWithProperty(propertyId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		return list.stream()
				.map(f -> {
					ForageDisponibilityDto dto = modelMapper.map(f, ForageDisponibilityDto.class);

					if (f.getDate() != null) {
						dto.setFormation(f.getDate().format(formatter));
					}

					if (f.getNumCows() != null) {
						dto.setNumCows(f.getNumCows().longValue());
					}

					dto.setCultivation(f.getForage());

					dto.setArea(f.getPicketArea() != null ? f.getPicketArea().toString() : null);

					return dto;
				})
				.toList();
	}

	@Override
	public void updateForage(Long propertyId, Long forageId, ForageUpdateDto updateDto) {
		ForageDisponibility forage = forageRepository.findByIdAndPropertyId(forageId, propertyId)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Forrageira não encontrada para esta propriedade"
				));

		if (updateDto.getCultivation() != null) {
			forage.setForage(updateDto.getCultivation());
		}

		if (updateDto.getArea() != null) {
			try {
				forage.setPicketArea(Float.valueOf(updateDto.getArea()));
			} catch (NumberFormatException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Área inválida");
			}
		}

		if (updateDto.getAverageCost() != null) {
			try {
				forage.setAverageCost(Float.valueOf(updateDto.getAverageCost()));
			} catch (NumberFormatException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Custo médio inválido");
			}
		}

		if (updateDto.getUsefulLife() != null) {
			try {
				forage.setUsefulLife(Long.valueOf(updateDto.getUsefulLife()));
			} catch (NumberFormatException e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vida útil inválida");
			}
		}

		if (updateDto.getFormation() != null) {
			try {
				LocalDate date = LocalDate.parse(updateDto.getFormation(), DateTimeFormatter.ISO_LOCAL_DATE);
				forage.setDate(date);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data inválida (use YYYY-MM-DD)");
			}
		}

		if (updateDto.getGrowthCycle() != null) {
			forage.setGrowthCycle(updateDto.getGrowthCycle());
		}

		forageRepository.save(forage);
	}

	@Override
	public ForageDisponibilityDto findDtoById(Long id) {
		ForageDisponibility forageEntity = forageRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado com ID: " + id));

		this.entityManager.refresh(forageEntity);

		ForageDisponibilityDto dto = modelMapper.map(forageEntity, ForageDisponibilityDto.class);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		dto.setCultivation(forageEntity.getForage());

		if (forageEntity.getDate() != null) {
			dto.setFormation(forageEntity.getDate().format(formatter));
		}

		dto.setArea(forageEntity.getPicketArea() != null ? forageEntity.getPicketArea().toString() : null);
		if (forageEntity.getNumCows() != null) {
			dto.setNumCows(forageEntity.getNumCows().longValue());
		}

		return dto;
	}
}