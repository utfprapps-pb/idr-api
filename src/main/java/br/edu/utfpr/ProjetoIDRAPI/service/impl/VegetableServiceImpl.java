package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import br.edu.utfpr.ProjetoIDRAPI.model.Vegetable;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetableRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableService;

@Service
public class VegetableServiceImpl implements VegetableService{
	private final VegetableRepository repository;
	
	@Override
	public Vegetable save(Vegetable vegetable) {
		return repository.save(vegetable);
	}
	
	public VegetableServiceImpl(VegetableRepository repository) {
		this.repository = repository;
	}

	@Override
	public Vegetable findOne(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public List<Vegetable> findAll() {
		return repository.findAll();
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Vegetable findByName(String name) {
		return repository.findByName(name);
	}
	
}
