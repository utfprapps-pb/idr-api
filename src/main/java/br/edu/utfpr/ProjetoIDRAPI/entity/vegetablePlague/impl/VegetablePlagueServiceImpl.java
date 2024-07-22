package br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague.VegetablePlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague.VegetablePlagueService;
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
