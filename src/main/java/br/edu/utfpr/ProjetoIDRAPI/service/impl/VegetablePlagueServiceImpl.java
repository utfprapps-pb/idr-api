package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.VegetablePlague;
import br.edu.utfpr.ProjetoIDRAPI.repository.VegetablePlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.VegetablePlagueService;

@Service
public class VegetablePlagueServiceImpl extends CrudServiceImpl<VegetablePlague, Long> implements VegetablePlagueService {

	private final VegetablePlagueRepository plagueRepository;
	
	public VegetablePlagueServiceImpl(VegetablePlagueRepository plagueRepository) {
		this.plagueRepository = plagueRepository;
	}

	@Override
	protected JpaRepository<VegetablePlague, Long> getRepository() {
		return this.plagueRepository;
	}

}
