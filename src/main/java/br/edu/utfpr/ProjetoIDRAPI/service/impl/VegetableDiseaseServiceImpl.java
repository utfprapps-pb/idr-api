package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetableDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableDiseaseService;

@Service
public class VegetableDiseaseServiceImpl extends CrudServiceImpl<VegetableDisease, Long> implements VegetableDiseaseService {

	private final VegetableDiseaseRepository vegetableDiseaseRepository;
	
	public VegetableDiseaseServiceImpl(VegetableDiseaseRepository vegetableDiseaseRepository) {
		this.vegetableDiseaseRepository = vegetableDiseaseRepository;
	}
	
	@Override
	protected JpaRepository<VegetableDisease, Long> getRepository() {
		return this.vegetableDiseaseRepository;
	}	
}
