package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import java.io.Serializable;
import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.search.SearchHandler;
import br.edu.utfpr.ProjetoIDRAPI.search.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public abstract class CrudServiceImpl <T, ID extends Serializable> implements CrudService<T, ID>{
	protected abstract JpaRepository<T, ID> getRepository();
	
	@Override
	public T save(T entity) {
		return getRepository().save(entity);
	}
	
	@Override
	public T findOne(ID id) {
		return getRepository().findById(id).orElse(null);
	}

	@Override
	public List<T> findAll() {
		return getRepository().findAll();
	}

	@Override
	public void delete(ID id) {
		getRepository().deleteById(id);
	}

	/**
	 * This method is not implemented by default.
	 * It should be implemented by the service that needs to use the search functionality.
	 * @return JpaSpecificationExecutor<T>
	 */
	public JpaSpecificationExecutor<T> getSpecExecutor() {
		return null;
	}

	public Page<T> search(SearchRequest request) {
		return new SearchHandler<>(getSpecExecutor()).handle(request);
	}
}
