package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.repository.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ForageDisponibilityService;

@Service
public class ForageDisponibilityServiceImpl implements ForageDisponibilityService{
	private final ForageDisponibilityRepository forageRepository;
	
	public ForageDisponibilityServiceImpl(ForageDisponibilityRepository forageRepository) {
		this.forageRepository = forageRepository;
	}
	
	@Override
	public ForageDisponibility findOne(Long id) {
		return forageRepository.findById(id).orElse(null);
	}

	@Override
	public List<ForageDisponibility> findAll() {
		return forageRepository.findAll();
	}

	@Override
	public ForageDisponibility save(ForageDisponibility forage) {
		return forageRepository.save(forage);
	}

	@Override
	public void delete(Long id) {
		forageRepository.deleteById(id);
	}

}
