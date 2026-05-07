package br.gov.pr.idr.legacy.entity.vegetableplague.impl;

import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;
import br.gov.pr.idr.legacy.entity.vegetableplague.VegetablePlague;
import br.gov.pr.idr.legacy.entity.vegetableplague.VegetablePlagueRepository;
import br.gov.pr.idr.legacy.entity.vegetableplague.VegetablePlagueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
