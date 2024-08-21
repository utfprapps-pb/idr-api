package br.edu.utfpr.ProjetoIDRAPI.entity.crud;

import br.edu.utfpr.ProjetoIDRAPI.search.request.SearchRequest;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public interface CrudService <T, ID extends Serializable> {

	T findOne(ID id);

	List<T> findAll();

	T save(T entity);

	void delete(ID id);

	Page<T> search(SearchRequest request);

}
