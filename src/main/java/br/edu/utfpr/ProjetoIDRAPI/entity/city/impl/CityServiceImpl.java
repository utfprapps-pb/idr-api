package br.edu.utfpr.ProjetoIDRAPI.entity.city.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.CityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.CityService;
import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

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

	/*
	//para realizar testes será possivel salvar e deletar cidades
	@Override
	public City save(City entity) {
		return null;
		//caso as ja estejam cadastradas em outro banco e não seja
		//necessario salvar deixar o return null
	}

	@Override
	public void delete(Long id) {
		System.out.println("NÃO É POSSÍVEL REALIZAR O DELETE DE CIDADES");
	}*/

	@Override
	public JpaSpecificationExecutor<City> getSpecExecutor() {
		return this.cityRepository;
	}
}
