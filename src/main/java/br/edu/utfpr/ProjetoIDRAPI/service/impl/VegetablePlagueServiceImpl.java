package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetablePlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetablePlagueService;

import java.util.List;

@Service
@Slf4j
public class VegetablePlagueServiceImpl extends CrudServiceImpl<VegetablePlague, Long> implements VegetablePlagueService {

	private final VegetablePlagueRepository plagueRepository;
	
	public VegetablePlagueServiceImpl(VegetablePlagueRepository plagueRepository) {
		this.plagueRepository = plagueRepository;
	}

	@Override
	protected JpaRepository<VegetablePlague, Long> getRepository() {
		return this.plagueRepository;
	}

	@Override
	public boolean saveListVegetablePlagues(List<VegetablePlague> vegetablePlagues) {
		boolean status = true;
		try {
			plagueRepository.saveAll(vegetablePlagues);
		} catch (Exception e){
			status = false;
			log.error(e.getMessage());
		}

		return status;
	}
}
