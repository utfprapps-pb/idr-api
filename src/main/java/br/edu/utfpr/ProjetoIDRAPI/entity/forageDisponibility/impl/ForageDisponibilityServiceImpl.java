package br.edu.utfpr.ProjetoIDRAPI.entity.forageDisponibility.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.forageDisponibility.ForageDisponibility;
import br.edu.utfpr.ProjetoIDRAPI.entity.forageDisponibility.ForageDisponibilityRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.forageDisponibility.ForageDisponibilityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ForageDisponibilityServiceImpl extends CrudServiceImpl<ForageDisponibility, Long>
		implements ForageDisponibilityService {

	private final ForageDisponibilityRepository forageRepository;
	
	public ForageDisponibilityServiceImpl(ForageDisponibilityRepository forageRepository) {
		this.forageRepository = forageRepository;
	}

	@Override
	protected JpaRepository<ForageDisponibility, Long> getRepository() {
		return this.forageRepository;
	}

}
