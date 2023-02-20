package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.repository.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;

@Service
public class CityServiceImpl extends CrudServiceImpl<City, Long> implements CityService {
	private final CityRepository cityRepository;
	
	public CityServiceImpl(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}

	@Override
	protected JpaRepository<City, Long> getRepository() {
		return this.cityRepository;
	}
	
	@Override
	public City findByName(String name) {
		return cityRepository.findByName(name);
	}

	@Override
	public City save(City entity) {
		return null;
	}

	@Override
	public void delete(Long id) {
		System.out.println("NÃO É POSSÍVEL REALIZAR O DELETE DE CIDADES");
	}
}
