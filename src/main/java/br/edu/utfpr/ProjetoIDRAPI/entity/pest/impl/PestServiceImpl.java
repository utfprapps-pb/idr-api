package br.edu.utfpr.ProjetoIDRAPI.entity.pest.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.Pest;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.PestRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.PestService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class PestServiceImpl extends CrudServiceImpl<Pest, Long> implements PestService {
	private final PestRepository pestRepository;
	
	public PestServiceImpl(PestRepository pestRepository) {
		this.pestRepository = pestRepository;
	}
	
	@Override
	public Pest findByName(String name) {
		return pestRepository.findByName(name);
	}

	@Override
	protected JpaRepository<Pest, Long> getRepository() {
		return this.pestRepository;
	}

	@Override
	public JpaSpecificationExecutor<Pest> getSpecExecutor() {
		return this.pestRepository;
	}
}
