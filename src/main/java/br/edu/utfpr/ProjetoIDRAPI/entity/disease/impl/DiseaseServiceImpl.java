package br.edu.utfpr.ProjetoIDRAPI.entity.disease.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.DiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.DiseaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

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
