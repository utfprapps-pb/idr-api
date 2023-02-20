package br.edu.utfpr.ProjetoIDRAPI.service;

import java.io.Serializable;
import java.util.List;

public interface CrudService <T, ID extends Serializable> {

	T findOne(ID id);

	List<T> findAll();

	T save(T entity);

	void delete(ID id);

}
