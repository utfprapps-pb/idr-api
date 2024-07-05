package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import br.edu.utfpr.ProjetoIDRAPI.model.Culture;
import br.edu.utfpr.ProjetoIDRAPI.repository.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.CultureService;

@Service
public class CultureServiceImpl extends CrudServiceImpl<Culture, Long> implements CultureService {
	private final CultureRepository cultureRepository;
	
	public CultureServiceImpl(CultureRepository cultureRepository) {
		this.cultureRepository = cultureRepository;
	}
	
	@Override
	public Culture findByName(String name) {
		return cultureRepository.findByCultureName(name);
	}

	@Override
	protected JpaRepository<Culture, Long> getRepository() {
		return this.cultureRepository;
	}

	@Override
	public JpaSpecificationExecutor<Culture> getSpecExecutor() {
		return this.cultureRepository;
	}
}
