package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetableRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableService;

@Service
public class VegetableServiceImpl extends CrudServiceImpl<Vegetable, Long> implements VegetableService {

	private final VegetableRepository vegetableRepository;

	@Override
	protected JpaRepository<Vegetable, Long> getRepository() {
		return this.vegetableRepository;
	}
	
	public VegetableServiceImpl(VegetableRepository vegetableRepository) {
		this.vegetableRepository = vegetableRepository;
	}

	@Override
	public Vegetable findByName(String name) {
		return vegetableRepository.findByName(name);
	}
	
}
