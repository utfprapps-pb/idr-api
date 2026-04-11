package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.CultivationPest;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.CultivationPestRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest.CultivationPestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CultivationPestServiceImpl extends CrudServiceImpl<CultivationPest, Long> implements CultivationPestService {

	private final CultivationPestRepository plagueRepository;
	
	public CultivationPestServiceImpl(CultivationPestRepository plagueRepository) {
		this.plagueRepository = plagueRepository;
	}

	@Override
	protected JpaRepository<CultivationPest, Long> getRepository() {
		return this.plagueRepository;
	}

	@Override
	public boolean saveListCultivationPest(List<CultivationPest> cultivationPests) {
		boolean status = true;
		try {
			plagueRepository.saveAll(cultivationPests);
		} catch (Exception e){
			status = false;
			log.error(e.getMessage());
		}

		return status;
	}
}
