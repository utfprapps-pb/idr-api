package br.edu.utfpr.ProjetoIDRAPI.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import br.edu.utfpr.ProjetoIDRAPI.model.VegetableDisease;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetableDiseaseRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetableDiseaseService;

import java.util.List;

@Service
@Slf4j
public class VegetableDiseaseServiceImpl extends CrudServiceImpl<VegetableDisease, Long> implements VegetableDiseaseService {

	private final VegetableDiseaseRepository vegetableDiseaseRepository;

	public VegetableDiseaseServiceImpl(VegetableDiseaseRepository vegetableDiseaseRepository) {
		this.vegetableDiseaseRepository = vegetableDiseaseRepository;
	}
	
	@Override
	protected JpaRepository<VegetableDisease, Long> getRepository() {
		return this.vegetableDiseaseRepository;
	}

	@Override
	public boolean saveListVegetableDiseases(List<VegetableDisease> vegetableDiseases) {
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println(vegetableDiseases);
		boolean status = true;
		try {
			vegetableDiseaseRepository.saveAll(vegetableDiseases);
		} catch (Exception e){
			status = false;
			log.error(e.getMessage());
		}

		return status;
	}
}
