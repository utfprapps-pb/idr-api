package br.edu.utfpr.ProjetoIDRAPI.entity.culture.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureFilter;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.culture.CultureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<Culture> search(CultureFilter filter, Pageable pageable) {
		Specification<Culture> spec = Specification.where(null);

		if (filter.getCultureName() != null && !filter.getCultureName().isEmpty()) {
			spec = spec.and((root, query, cb) ->
					cb.like(cb.lower(root.get("cultureName")), "%" + filter.getCultureName().toLowerCase() + "%")
			);
		}

		if (filter.getCultureType() != null) {
			spec = spec.and((root, query, cb) ->
					cb.equal(root.get("cultureType"), filter.getCultureType())
			);
		}

		return cultureRepository.findAll(spec, pageable);
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
