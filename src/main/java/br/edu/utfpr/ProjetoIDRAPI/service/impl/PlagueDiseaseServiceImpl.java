package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.PlagueDisease;
import br.edu.utfpr.ProjetoIDRAPI.repository.PlagueDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.PlagueDiseaseService;

@Service
public class PlagueDiseaseServiceImpl implements PlagueDiseaseService {
	private final PlagueDiseaseRepository plagueRepository;
	
	public PlagueDiseaseServiceImpl(PlagueDiseaseRepository plagueRepository) {
		this.plagueRepository = plagueRepository;
	}
	
	@Override
	public PlagueDisease findOne(Long id) {
		return plagueRepository.findById(id).orElse(null);
	}

	@Override
	public List<PlagueDisease> findAll() {
		return plagueRepository.findAll();
	}

	@Override
	public PlagueDisease save(PlagueDisease plagueDisease) {
		return plagueRepository.save(plagueDisease);
	}

	@Override
	public void delete(Long id) {
		plagueRepository.deleteById(id);
	}

}
