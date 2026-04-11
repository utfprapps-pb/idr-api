package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.impl;


import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.CultivationDisease;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.CultivationDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease.CultivationDiseaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CultivationDiseaseServiceImpl extends CrudServiceImpl<CultivationDisease, Long> implements CultivationDiseaseService {

	private final CultivationDiseaseRepository cultivationDiseaseRepository;

	public CultivationDiseaseServiceImpl(CultivationDiseaseRepository cultivationDiseaseRepository) {
		this.cultivationDiseaseRepository = cultivationDiseaseRepository;
	}
	
	@Override
	protected JpaRepository<CultivationDisease, Long> getRepository() {
		return this.cultivationDiseaseRepository;
	}

	@Override
	public boolean saveListCultivationDiseases(List<CultivationDisease> cultivationDiseases) {
		boolean status = true;
		try {
			cultivationDiseaseRepository.saveAll(cultivationDiseases);
		} catch (Exception e){
			status = false;
			log.error(e.getMessage());
		}

		return status;
	}
}
