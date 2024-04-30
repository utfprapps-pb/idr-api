package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.Disease;
import br.edu.utfpr.ProjetoIDRAPI.repository.DiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.DiseaseService;

@Service
public class DiseaseServiceImpl extends CrudServiceImpl<Disease, Long> implements DiseaseService {
	private final DiseaseRepository diseaseRepository;

	public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
		this.diseaseRepository = diseaseRepository;
	}
	
	@Override
	public Disease findByName(String name) {
		return diseaseRepository.findByDiseaseName(name);
	}

	@Override
	protected JpaRepository<Disease, Long> getRepository() {
		return this.diseaseRepository;
	}

	@Override
	public JpaSpecificationExecutor<Disease> getSpecExecutor() {
		return this.diseaseRepository;
	}
}
