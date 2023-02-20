package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.repository.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.ForageDisponibilityService;

@Service
public class ForageDisponibilityServiceImpl extends CrudServiceImpl<ForageDisponibility, Long>
		implements ForageDisponibilityService{

	private final ForageDisponibilityRepository forageRepository;
	
	public ForageDisponibilityServiceImpl(ForageDisponibilityRepository forageRepository) {
		this.forageRepository = forageRepository;
	}

	@Override
	protected JpaRepository<ForageDisponibility, Long> getRepository() {
		return this.forageRepository;
	}

}
