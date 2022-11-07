package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.City;
import br.edu.utfpr.ProjetoIDRAPI.repository.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.CityService;

@Service
public class CityServiceImpl implements CityService{
	private final CityRepository cityRepository;
	
	public CityServiceImpl(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}

	@Override
	public City findOne(Long id) {
		return cityRepository.findById(id).orElse(null);
	}
	
	@Override
	public City findByName(String name) {
		return cityRepository.findByName(name);
	}

	@Override
	public List<City> findAll() {
		return cityRepository.findAll();
	}

	@Override
	public City save(City entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
}
