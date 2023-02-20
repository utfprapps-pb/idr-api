package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.repository.PlagueDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PlagueDiseaseService;

@Service
public class PlagueDiseaseServiceImpl extends CrudServiceImpl<PlagueDisease, Long> implements PlagueDiseaseService {

	private final PlagueDiseaseRepository plagueRepository;
	
	public PlagueDiseaseServiceImpl(PlagueDiseaseRepository plagueRepository) {
		this.plagueRepository = plagueRepository;
	}

	@Override
	protected JpaRepository<PlagueDisease, Long> getRepository() {
		return this.plagueRepository;
	}

}
