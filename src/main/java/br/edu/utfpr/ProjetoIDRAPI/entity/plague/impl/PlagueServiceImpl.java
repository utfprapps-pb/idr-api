package br.edu.utfpr.ProjetoIDRAPI.entity.plague.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.PlagueRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.PlagueService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class PlagueServiceImpl extends CrudServiceImpl<Plague, Long> implements PlagueService {
	private final PlagueRepository plagueRepository;
	
	public PlagueServiceImpl(PlagueRepository plagueRepository) {
		this.plagueRepository = plagueRepository;
	}
	
	@Override
	public Plague findByName(String name) {
		return plagueRepository.findByPlagueName(name);
	}

	@Override
	protected JpaRepository<Plague, Long> getRepository() {
		return this.plagueRepository;
	}

	@Override
	public JpaSpecificationExecutor<Plague> getSpecExecutor() {
		return this.plagueRepository;
	}
}
