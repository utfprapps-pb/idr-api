package br.edu.utfpr.ProjetoIDRAPI.entity.culture.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

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
